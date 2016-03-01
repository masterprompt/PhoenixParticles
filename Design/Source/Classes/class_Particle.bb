	;**************************************************************************
	; CLASS: cParticle
	;	Class Purpose -
	;		-Governs the Particle Objects
	;
	;		-Base Classes:
	;			cVector
	;			cParticleDescription
	;**************************************************************************
	;include "class_Vector.bb"
	
	Global cParticle_Count
	Global cParticle_Pause
	Global cParticle_MS
	Global cParticle_MSLastSample
	
	Type cParticle
		Field iID%
		Field hPivot%
		Field hCube
		
		Field iParticleLifeCycleTime%
		Field fGravity#
		Field Alpha#
		Field Position.cVector
		Field Scale.cVector
		Field Rotation.cVector
		
		Field Velocity#
		Field Vector.cVector
		Field StartAlpha#
		Field EndAlpha#
		Field StartScale.cVector
		Field Description.cParticleDescription
		
		Field iStartTime
	End Type
	;---------------Public Methods-----------------------------
	Function cParticle_New.cParticle(  )
		Local This.cParticle
		This					=New cParticle
		This\iID				=Handle( This )
		This\Vector				=New cVector
		This\Position			=New cVector
		This\Scale				=New cVector
		This\iStartTime			=cParticle_Millisecs()
		This\StartScale			=New cVector
		This\hPivot				=CreatePivot()
		;This\hCube				=CreateCube(This\hPivot)
		;EntityColor(This\hCube,rand(0,255),rand(0,255),rand(0,255))
		
		cParticle_Count			=cParticle_Count+1
		Return( This )
	End Function
	Function cParticle_Delete( This.cParticle )
		Delete(This\StartScale)
		Delete(This\Scale)
		Delete(This\Position)
		Delete( This\Vector	)
		FreeEntity(This\hPivot)
		Delete( This )
		cParticle_Count			=cParticle_Count-1
	End Function
	
	Function cParticle_Start( This.cParticle )
		;--After description is set, call this to start the particle--

		This\Vector\X			=This\Description\StartMin\Vector\X + (Rnd(0,1) * (This\Description\StartMax\Vector\X - This\Description\StartMin\Vector\X))
		This\Vector\Y			=This\Description\StartMin\Vector\Y + (Rnd(0,1) * (This\Description\StartMax\Vector\Y - This\Description\StartMin\Vector\Y))
		This\Vector\Z			=This\Description\StartMin\Vector\Z + (Rnd(0,1) * (This\Description\StartMax\Vector\Z - This\Description\StartMin\Vector\Z))
		This\Velocity			=This\Description\StartMin\Velocity + (Rnd(0,1) * (This\Description\StartMax\Velocity - This\Description\StartMin\Velocity))
		
		
		This\StartScale\X		=(Rnd(0,1) * (This\Description\EndMax\Scale\X - This\Description\StartMax\Scale\X))
		This\StartScale\Y		=(Rnd(0,1) * (This\Description\StartMax\Scale\Y - This\Description\StartMin\Scale\Y))
		This\Scale\X			=(Rnd(0,1) * (This\Description\EndMax\Scale\X - This\Description\EndMin\Scale\X))
		This\Scale\Y			=(Rnd(0,1) * (This\Description\EndMax\Scale\Y - This\Description\EndMin\Scale\Y))
		This\StartAlpha			=This\Description\StartMin\Alpha + (Rnd(0,1) * (This\Description\StartMax\Alpha - This\Description\StartMin\Alpha))
		This\EndAlpha			=This\Description\EndMin\Alpha + (Rnd(0,1) * (This\Description\EndMax\Alpha - This\Description\EndMin\Alpha))
		This\Alpha				=This\EndAlpha - This\StartAlpha
		cVector_Normalize( This\Vector )
		This\Vector\X			=This\Vector\X * This\Velocity
		This\Vector\Y			=This\Vector\Y * This\Velocity
		This\Vector\Z			=This\Vector\Z * This\Velocity
		
	End Function
	
	Function cParticle_Update()
		Local This.cParticle
		For This = each cParticle
			ThisTime#					=cParticle_Millisecs() - This\iStartTime
			ThisTimeP#					=ThisTime# / float(This\Description\iLifeTimeMS)
			if ThisTime > This\Description\iLifeTimeMS then
				cParticle_Delete( This.cParticle )
			Else
				if cParticle_Pause = false then
					This\Position\X				=This\Position\X + This\Vector\X
					This\Position\Y				=This\Position\Y + (This\Vector\Y - (This\Description\Gravity * (ThisTime * .001)))
					This\Position\Z				=This\Position\Z + This\Vector\Z
				endif
				This\Alpha#					=This\StartAlpha + (ThisTimeP# * (This\EndAlpha - This\StartAlpha))
				ThisScaleX#					=(This\Description\StartMax\Scale\X+(ThisTimeP# * (This\Description\EndMax\Scale\X - This\Description\StartMax\Scale\X))) / 2
				ThisScaleY#					=(This\Description\StartMax\Scale\Y+(ThisTimeP# * (This\Description\EndMax\Scale\Y - This\Description\StartMax\Scale\Y))) / 2
				TFormPoint(This\Position\X,This\Position\Y,This\Position\Z, 0, This\Description\Material\Mesh)
			
				ThisV1						=AddVertex(This\Description\Material\Surface,TFormedX() - ThisScaleX#,TFormedY() - ThisScaleY#,TFormedZ(),	This\Description\Material\U1,This\Description\Material\V2)
				ThisV2						=AddVertex(This\Description\Material\Surface,TFormedX() + ThisScaleX#,TFormedY() - ThisScaleY#,TFormedZ(),	This\Description\Material\U2,This\Description\Material\V2)
				ThisV3						=AddVertex(This\Description\Material\Surface,TFormedX() + ThisScaleX#,TFormedY() + ThisScaleY#,TFormedZ(),	This\Description\Material\U2,This\Description\Material\V1)
				ThisV4						=AddVertex(This\Description\Material\Surface,TFormedX() - ThisScaleX#,TFormedY() + ThisScaleY#,TFormedZ(),	This\Description\Material\U1,This\Description\Material\V1)
				AddTriangle(This\Description\Material\Surface, ThisV1, ThisV4, ThisV3 )
				AddTriangle(This\Description\Material\Surface, ThisV3, ThisV2, ThisV1 )
				;MoveEntity(This\hPivot,This\Vector\X,ThisY,This\Vector\Z)
				VertexColor(This\Description\Material\Surface,ThisV1,255,255,255,This\Alpha)
				VertexColor(This\Description\Material\Surface,ThisV2,255,255,255,This\Alpha)
				VertexColor(This\Description\Material\Surface,ThisV3,255,255,255,This\Alpha)
				VertexColor(This\Description\Material\Surface,ThisV4,255,255,255,This\Alpha)
				;EntityAlpha(This\hCube,This\Alpha#)
			Endif
		Next
	End Function
	
	Function cParticle_Millisecs(  )
		if cParticle_Pause = true then
			cParticle_MSLastSample				=Millisecs()
		Endif
		cParticle_MS						=cParticle_MS + (Millisecs() - cParticle_MSLastSample)
		cParticle_MSLastSample				=Millisecs()
		return( cParticle_MS )
	End Function




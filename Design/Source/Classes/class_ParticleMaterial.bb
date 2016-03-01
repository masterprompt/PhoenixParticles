	;**************************************************************************
	; CLASS: cParticleMaterial
	;	Class Purpose -
	;		-Governs the Particle Materials Objects
	;
	;		-Base Classes:
	;			cVector
	;**************************************************************************
	;include "class_Vector.bb"
	
	Type cParticleMaterial
		Field iID
		Field Camera
		Field Mesh
		Field Surface
		Field Brush
		Field Image
		Field U1#, U2
		Field V1#, V2#
	End Type
	;---------------Public Methods-----------------------------
	Function cParticleMaterial_New.cParticleMaterial()
		Local This.cParticleMaterial
		This					=New cParticleMaterial
		This\iID				=Handle( This )
		This\Mesh				=CreateMesh()
		This\Surface			=CreateSurface(This\Mesh)
		This\Brush				=CreateBrush()
		EntityFX(This\Mesh,1+2+4+32)
		Return( This )
	End Function
	Function cParticleMaterial_Delete( This.cParticleMaterial )
		FreeEntity(This\Mesh)
		Delete( This )
	End Function
	
	Function cParticleMaterial_Start( This.cParticleMaterial )
		BrushTexture(This\Brush,This\Image	)
		EntityParent(This\Mesh, This\Camera)
		PaintSurface(This\Surface, This\Brush)
		;PaintEntity(This\Mesh, This\Brush)
	End Function
	
	Function cParticleMaterial_Update()
		Local This.cParticleMaterial
		For This = each cParticleMaterial
			ClearSurface(This\Surface,True,True)
		Next
	End Function

	

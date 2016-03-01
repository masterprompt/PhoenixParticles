	;**************************************************************************
	; CLASS: cParticleEmitter
	;	Class Purpose -
	;		-Governs the Particle Emitter Objects
	;
	;		-Base Classes:
	;			cVector
	;			cParticleDescription
	;**************************************************************************
	;include "class_Vector.bb"
	
	Type cParticleEmitter
		Field iID%
		Field Pivot%
		Field iLifeCycleTime%
		Field iParticleLifeCycleTime%
		Field FireRate%
		Field BurstRate%
		
		Field Description.cParticleDescription
		
		Field bActive
		Field iStartTime
		Field iLastFire
		
		Field oUpdater.cParticleEmitter_Updater
	End Type
	Type cParticleEmitter_Updater
		Field iID%
		Field oEmitter.cParticleEmitter
	End Type
	;---------------Public Methods-----------------------------
	Function cParticleEmitter_New.cParticleEmitter(  )
		Local This.cParticleEmitter
		;--Create The Emitter Object--
		This					=New cParticleEmitter
		This\iID				=Handle( This )
		;--Create The Emitter Pivot--
		This\Pivot				=CreatePivot()
		Return( This )
	End Function
	Function cParticleEmitter_Delete( This.cParticleEmitter )
		If This\oUpdater <> null then
			Delete( This\oUpdater )
		Endif
		FreeEntity(This\Pivot)
		Delete( This )
	End Function
	
	Function cParticleEmitter_Start( This.cParticleEmitter )
		If This\oUpdater = null then
			;--Set Emitter Active--
			This\bActive				=True
			This\iStartTime				=Millisecs()
			;--Create The Updater and Assign The Emitter--
			This\oUpdater				=New cParticleEmitter_Updater
			This\oUpdater\oEmitter		=This
		Endif
	End Function

	Function cParticleEmitter_Stop( This.cParticleEmitter )
		If This\oUpdater <> null then
			This\bActive 				=False
			Delete( This\oUpdater )
		Endif
	End Function

	Function cParticleEmitter_Update()
		Local This.cParticleEmitter_Updater
		For This = each cParticleEmitter_Updater
			cParticleEmitter_UpdateParticleEmitter( This\oEmitter )
		Next
	End Function
	
	Function cParticleEmitter_UpdateParticleEmitter( This.cParticleEmitter )
		Local ThisParticle.cParticle
		if This\bActive then
			if This\iLifeCycleTime > 0 AND cParticle_Millisecs(  ) - This\iStartTime >= This\iLifeCycleTime then
				cParticleEmitter_Stop( This.cParticleEmitter )
			Else
				if cParticle_Millisecs() - This\iLastFire >= This\FireRate then
					This\iLastFire    			=cParticle_Millisecs(  )
					;FIRE
					For ThisFire = 1 to This\BurstRate + 1
						ThisParticle				=cParticle_New(  )
						ThisParticle\Position\X		=EntityX(This\Pivot,1)
						ThisParticle\Position\Y		=EntityY(This\Pivot,1)
						ThisParticle\Position\Z		=EntityZ(This\Pivot,1)
						ThisParticle\Description	=This\Description
						cParticle_Start( ThisParticle)
					Next
				Endif
			endif
		endif
	End Function
	


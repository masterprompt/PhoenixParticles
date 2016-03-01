	;**************************************************************************
	; CLASS: cParticleDescription
	;	Class Purpose -
	;		-Describes Particle Behavior
	;
	;		-Base Classes:
	;			cVector
	;**************************************************************************
	;include "class_Vector.bb"
	
	Type cParticleDescription
		Field iID%
		
		Field Material.cParticleMaterial
		
		Field iLifeTimeMS%
		Field Gravity#

		Field StartMin.cParticleDescriptionPoint
		Field StartMax.cParticleDescriptionPoint
		Field EndMin.cParticleDescriptionPoint
		Field EndMax.cParticleDescriptionPoint

	End Type
	Type cParticleDescriptionPoint
		Field Alpha#
		Field Scale.cVector
		Field Rotation.cVector
		Field Velocity#
		Field Vector.cVector
	End Type
	;---------------Public Methods-----------------------------
	Function cParticleDescription_New.cParticleDescription()
		Local This.cParticleDescription
		This					=New cParticleDescription
		This\iID				=Handle( This )
		This\StartMin			=cParticleDescriptionPoint_New()
		This\StartMax			=cParticleDescriptionPoint_New()
		This\EndMin				=cParticleDescriptionPoint_New()
		This\EndMax				=cParticleDescriptionPoint_New()
		Return( This )
	End Function
	Function cParticleDescription_Delete( This.cParticleDescription )
		cParticleDescriptionPoint_Delete( This\StartMin	 )
		cParticleDescriptionPoint_Delete( This\StartMax )
		cParticleDescriptionPoint_Delete( This\EndMin )
		cParticleDescriptionPoint_Delete( This\EndMax )
		Delete( This )
	End Function
	
	Function cParticleDescriptionPoint_New.cParticleDescriptionPoint()
		Local This.cParticleDescriptionPoint
		This					=New cParticleDescriptionPoint
		This\Scale				=New cVector
		This\Rotation			=New cVector
		This\Vector				=New cVector
		Return( This )
	End Function
	Function cParticleDescriptionPoint_Delete( This.cParticleDescriptionPoint )
		Delete( This\Scale )
		Delete( This\Rotation )
		Delete( This\Vector )
		Delete( This )
	End Function

include "Main_Definitions.bb"
Seedrnd(Millisecs())
AppTitle("Phoenix Particles v"+BuildVersion$)
ThisGraphics.cGraphics					=cGraphics_New.cGraphics()
cGraphics_SetMode( ThisGraphics, cGraphics_FindMode( ThisGraphics, 1024,768 ) , True )
ThisYaw							=CreatePivot()
ThisPitch						=CreatePivot(ThisYaw)
Camera 							=CreateCamera(ThisPitch)
CameraRange(Camera,1,1000)

Cube							=CreateCube()
ScaleEntity(Cube,5,5,5)

AmbientLight(100,100,100)
Light							=Createlight(0)
MoveEntity(Light,40,100,-60)

ThisImage						=LoadTexture(CurrentDir$+"\Data\particles.png",2)

ThisMaterial.cParticleMaterial				=cParticleMaterial_New.cParticleMaterial()
ThisMaterial\Camera							=Camera
ThisMaterial\Image							=ThisImage
ThisMaterial\U1#							=.3333
ThisMaterial\V1#							=.3333
ThisMaterial\U2#							=.6666
ThisMaterial\V2#							=.6666
cParticleMaterial_Start( ThisMaterial )


ThisDescription.cParticleDescription		=cParticleDescription_New.cParticleDescription()
ThisDescription\Material					=ThisMaterial
ThisDescription\iLifeTimeMS					=3000

ThisDescription\StartMin\Vector\X			=-.5
ThisDescription\StartMin\Vector\Y			=1
ThisDescription\StartMin\Vector\Z			=-.5
ThisDescription\StartMin\Velocity			=.2
ThisDescription\StartMin\Alpha				=1


ThisDescription\StartMax\Vector\X			=.5
ThisDescription\StartMax\Vector\Y			=1
ThisDescription\StartMax\Vector\Z			=.5
ThisDescription\StartMax\Velocity			=1
ThisDescription\StartMax\Alpha				=1
ThisDescription\StartMax\Scale\X			=1
ThisDescription\StartMax\Scale\Y			=1

ThisDescription\EndMin\Alpha				=0
ThisDescription\EndMax\Alpha				=0
ThisDescription\EndMax\Scale\X				=5
ThisDescription\EndMax\Scale\Y				=5

ThisDescription\Gravity						=.5

ThisEmitter.cParticleEmitter				=cParticleEmitter_New.cParticleEmitter(  )
ThisEmitter\Description						=ThisDescription
ThisEmitter\FireRate						=100
ThisEmitter\BurstRate						=1
cParticleEmitter_Start( ThisEmitter )

MoveEntity(Camera,0,0,-50)

While not keydown( 1 )
	if keydown(3) and keytrig=0 then
		keytrig=1
		ThisEmitter.cParticleEmitter				=cParticleEmitter_New.cParticleEmitter(  )
		ThisEmitter\Description						=ThisDescription
		ThisEmitter\FireRate						=100
		ThisEmitter\BurstRate						=1
		cParticleEmitter_Start( ThisEmitter )
		ThisRand=20
		PositionEntity(ThisEmitter\Pivot, rnd(-ThisRand,ThisRand), rnd(-ThisRand,ThisRand), rnd(-ThisRand,ThisRand))
	endif
	if keydown(3) = 0 then keytrig=0
	if keydown(203) then turnentity(ThisYaw,0,-10,0)
	if keydown(205) then turnentity(ThisYaw,0,10,0)
	
	if keydown(200) then turnentity(ThisPitch,-10,0,0)
	if keydown(208) then turnentity(ThisPitch,10,0,0)
	MoveMouse(GraphicsWidth()/2, GraphicsHeight()/2)
	ThisStartTime					=Millisecs()
	cParticle_Pause					=Keydown(2)
	cParticleMaterial_Update()
	cParticleEmitter_Update()
	cParticle_Update()
	ThisScanTime					=Millisecs() - ThisStartTime
	Renderworld()
	Text 0,0,"Particles:"+cParticle_Count
	Text 0,10,"Scan:"+ThisScanTime
	Flip()
Wend
cGraphics_Delete( ThisGraphics )
End

;Version History
;+Added Updating Object to Emitter Class - Loops only through active emitters
;+Added Particle Class
;+Added Particle Description Class to pass description Info To Particles Without Linking Emitter and Particle


;Create Material
;	Define Material Parameters
;		-Size of Image
;		-Location of Sprite Image on main image
;Create Emitter
;	Pivot is created and refferenced by ID
;	Start Emitter
;		Define Parameters of emitter
;			-Material
;			-Gravity
;			-Emitter Life Cycle Time
;			-Particle Life Cycle Time
;			-Emit Rate
;			-Velocity Vector
;			-Life Cycle Alpha
;			-Life Cycle Scale
;			-Life Cycle Rotation
;			-Life Cycle Randomizations
;		Create Mesh
;		Emitt Particles
;			Unused particles go in a repository for memory efficiency
;	Stop Emitter
;		Destroy Mesh
;		Destroy Particle Repository
;Destroy Emitter
;	Destroy Pivot
;	Free Memory For Emitter
;Destroy Material
;	Free Memory for Material

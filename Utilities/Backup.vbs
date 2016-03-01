'-----------------------Automated Project Backup----------------------------
'		written by Casey T Kerr 12/12/07
'---------------------------------------------------------------------------

Set objArgs 			=WScript.Arguments
Set objShell 			=WScript.CreateObject("WScript.Shell")
Set objFSO			=CreateObject("Scripting.FileSystemObject")

'Check for config file
if objFSO.FileExists("Config\ProjectInfo.txt") = true then
	Set objFile = objFSO.OpenTextFile("Config\ProjectInfo.txt")
	sCompanyName			=objFile.ReadLine
	sProjectName			=objFile.ReadLine
	MakePath ThisPath
	objFile.Close
	CreateBackup(sCompanyName & "_" & sProjectName & "_BAK_Date.zip")
	wscript.echo "Backup Complete!"
Else
	wscript.echo "Backup Failed!  No Project Info File!"
End if

Function CreateBackup(sFileName)
	sBackupFileName			=Replace(sFileName,"Date",Now())
	sBackupFileName			=Replace(sBackupFileName," ","_")
	sBackupFileName			=Replace(sBackupFileName,"/","-")
	sBackupFileName			=Replace(sBackupFileName,":","-")

	sWinZipDir			=objFSO.GetAbsolutePathName( objShell.CurrentDirectory & "\..\..\..\..\Apps\Winzip" )
	sProjectDir			=objFSO.GetAbsolutePathName( objShell.CurrentDirectory & "\..\*.*" )
	sProjBackupDir			=objFSO.GetAbsolutePathName( objShell.CurrentDirectory & "\..\..\..\Backups" )
	MakePath sProjBackupDir
	
	'Start Winzip
	objShell.Run sWinZipDir & "\WZZIP.EXE -rp " & sProjBackupDir & "\" & sBackupFileName & " " & sProjectDir

	'Create Config File
	MakePath objShell.CurrentDirectory & "\Config"
	Set objBuildFile		=objFSO.OpenTextFile(objShell.CurrentDirectory & "\Config\Backup.txt",2,2)
	objBuildFile.WriteLine(Now())
	objBuildFile.WriteLine(sBackupFileName)
	objBuildFile.Close
End Function





Function MakePath(sPath)
    MakePath = False
    If Not objFSO.DriveExists(objFSO.GetDriveName(sPath)) Then Exit Function
    If objFSO.FolderExists(sPath) Then
        MakePath = True
        Exit Function
    End if
    If Not MakePath(objFSO.GetParentFolderName(sPath)) Then Exit function
    On Error Resume next
    objFSO.CreateFolder sPath
    MakePath = objFSO.FolderExists(sPath)
End function
# SmartKaYa
Metamodel, metadata-driven platform
# configuration
1.After downloading the code, import it as a Maven project
   

2.Create a new KayaConfig.properties file under Path src/main/Resources

	# Windows Settings
	
	#Log Path
	kayaMetaModelParseLogPath=C:/workspace/KayaModel.log
	
	# Model file (binary :.mga XML:.xme)
	
	#KayaModelFile path
	baseModel.basePath=C:/Users/LiangChen/Desktop/GME/
	
	#KayaModel（3.0:0001.mga    : before is version information, can be arbitrary: after is the actual model file）
	
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
	
	# Runtime (dev: development, parsing MGA binary files, prod: production, parsing XME files, Windows supports both)
	
	# mode=dev
	mode=prod
	

    #Linux,Macos,Unix

    #Log Path
    kayaMetaModelParseLogPath=/Users/log/KayaModel.log

	baseModel.basePath=C:/Users/Chljapan/Desktop/GME/
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
   
    # Runtime (dprod: production mode, parsing XME files, only supported)
    mode=prod

 3.This step is required when you are developing on Windows.
 
   Copy the JAUT.DLL file to the appropriate location and set the system parameter PATH to point to the location of the DLL file.
 
   If you are using a Java 32-bit runtime environment, you will need to select a 32-bit link library file size of around 180K.

   If you're using a 64-bit Java runtime, you'll need to select a 64-bit link library with a file size of around 460K.
 
 4.With the above configuration, you have your own model-driven platform.

 
 5.You can follow the development manual in the KayaModel directory to build and run your business model.

 
	 #Contact Information:: 

          WeChat：Chljapan  PS:KaYa

    Email：chljapan@hotmail.com
    
    
 
进一步开发预想：

 Consider the advantages of multi-platform development, and consider rolling out a Web server-based version of the development tool in the future.

 

# SmartKaYa
Metamodel, metadata-driven platform
# 使用
1.下载代码后，作为Maven 项目导入

2.在 src/main/Resources 下新建kayaconfig.properties文件

	Windows设置如下：
	#Log Path
	kayaMetaModelParseLogPath=C:/workspace/KayaModel.log
	
	# 模型文件（二进制:.mga XML：.mga）
	#KayaModelFile path
	baseModel.basePath=C:/Users/LiangChen/Desktop/GME/
	#KayaModel（3.0:0001.mga    ：前为版本信息，可以任意  ：后面是实际的模型文件）
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
	# 运行方式 (dev:开发方式，解析的是mga二进制文件 prod:生产方式，解析的是xme文件，Windows支持两种方式)
	# mode=dev
	mode=prod
	

   Linux,Macos,Unix
   
    #Log Path
    kayaMetaModelParseLogPath=/Users/log/KayaModel.log

	baseModel.basePath=C:/Users/Chljapan/Desktop/GME/
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
   
    # 运行方式 (dprod:生产方式，解析的是xme文件,仅支持此方式)
    mode=prod

 3.当你在Windows系统下开发的时候需要这一步配置。
 
   将JAUT.dll文件拷到适当位置，设置系统参数path指向dll文件位置。
   如果你利用的是java 32位的运行环境，需要选择文件大小为180K左右的32位链接库文件。
   如果你领用的是java 64位的运行环境，需要选择文件大小为460K左右的64位链接库文件。
   
 4.经过以上的配置，你已经拥有你自己的模型驱动平台了。
 
 PS一部分的优化需要大家自己试试，比如说为数据库建索引（自己动手试试）
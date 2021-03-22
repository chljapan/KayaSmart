# SmartKaYa
Metamodel, metadata-driven platform
#具体参照KaYaModel/SmartKAYA开发手册.docx

# 使用
1.下载代码后，作为Maven 项目导入
   

2.在 src/main/Resources 下新建kayaconfig.properties文件

	# Windows设置如下
	
	#Log Path
	kayaMetaModelParseLogPath=C:/workspace/KayaModel.log
	
	# 模型文件（二进制:.mga XML：.xme）　
	
	#KayaModelFile path
	baseModel.basePath=C:/Users/LiangChen/Desktop/GME/
	
	#KayaModel（3.0:0001.mga    ：前为版本信息，可以任意  ：后面是实际的模型文件）
	
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
	
	# 运行方式 (dev:开发方式，解析的是mga二进制文件 prod:生产方式，解析的是xme文件，Windows支持两种方式)　
	
	# mode=dev
	mode=prod
	

    #Linux,Macos,Unix
    # 日志文件保存路径
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

 
 5.你可以按照KaYaModel目录下的开发手册来完成你的业务模型构建以及运行。

 
 PS一部分的优化需要大家自己试试，比如说为数据库建索引（自己动手试试）

 
	 #联系方式: 

          微信：Chljapan  注明KaYa

    Email：chljapan@hotmail.com
    
    
 
进一步开发预想：

 考虑多平台开发的优势，将来也考虑推出基于Web服务器版本的开发工具，但是需要Node.js 的高级技术人员才能实现。有想投资参与的同学可以联系我（微信：chljapan）。注意不是普通的Nodejs开发者，需要高手才行。目前已经实现部分的建模功能，但是解析部分有一些问题。欢迎讨论。

 

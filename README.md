# SmartKaYa
Metamodel, metadata-driven platform
# 使用
1.下载代码后，作为Maven 项目导入
   ダウンロードしてから、Mavenプロジェクトにコンバーターしてください。
   

2.在 src/main/Resources 下新建kayaconfig.properties文件
　　パスsrc/main/ResourcesにKayaのコンフィグファイルを新規作成してください。

	# Windows设置如下
	# Windowsの場合では
	
	#Log Path
	kayaMetaModelParseLogPath=C:/workspace/KayaModel.log
	
	# 模型文件（二进制:.mga XML：.xme）　
	# モデルファイル（バイナリーファイル：.mga  XML:.xme）
	
	#KayaModelFile path
	baseModel.basePath=C:/Users/LiangChen/Desktop/GME/
	
	#KayaModel（3.0:0001.mga    ：前为版本信息，可以任意  ：后面是实际的模型文件）
	# (':'前の部分はモデルバージョン、後ろはモデルファイル名です)
	
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
	
	# 运行方式 (dev:开发方式，解析的是mga二进制文件 prod:生产方式，解析的是xme文件，Windows支持两种方式)　
	# ランタイム方式 (dev:開発モード，バイナリーファイルmgaを利用する　 prod:デプロイモード，xmlファイルxmeを利用する，osシステムはWindowsの場合では両方サポートする、LINUX，Unix，MAC
	# の場合ではProdモードで利用してください)　
	
	# mode=dev
	mode=prod
	

    #Linux,Macos,Unix
    # 日志文件保存路径
    # ログファイル保存場所
    #Log Path
    kayaMetaModelParseLogPath=/Users/log/KayaModel.log

	baseModel.basePath=C:/Users/Chljapan/Desktop/GME/
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
   
    # 运行方式 (dprod:生产方式，解析的是xme文件,仅支持此方式)
    # ランタイム方式 (prod:デプロイモード，xmlファイルxmeを利用する，LINUX，Unix，MACの場合ではこのモードしか利用できない)　
    mode=prod

 3.当你在Windows系统下开发的时候需要这一步配置。
 　　Windowsを利用して、開発を行う際、リアルタイムでモデルを確認したい場合（開発モード：dev）では下記の設定は必須です。
 
   将JAUT.dll文件拷到适当位置，设置系统参数path指向dll文件位置。
 JAUT.dllファイルを好きな場所にコピーし、システムPathに追加する
 
   如果你利用的是java 32位的运行环境，需要选择文件大小为180K左右的32位链接库文件。
 Windowsは32ビットの場合ではファイルサイズは180KBのライブラリファイルを利用してください。
   如果你领用的是java 64位的运行环境，需要选择文件大小为460K左右的64位链接库文件。
 Windowsは64ビットの場合ではファイルサイズは460KBのライブラリファイルを利用してください。
 
 4.经过以上的配置，你已经拥有你自己的模型驱动平台了。
 　上記の配置でモデル駆動プラートフォームは完成です。
 
 PS一部分的优化需要大家自己试试，比如说为数据库建索引（自己动手试试）
 
	 #联系方式: 
	 #問い合わせ
          微信：Chljapan  注明KaYa
    　　　ウェチャット：Chljapan　　Kayaをコメント入れ
    Email：chljapan@hotmail.com
    
    
 
进一步开发预想：
これからの予定：
 考虑多平台开发的优势，将来也考虑推出基于Web服务器版本的开发工具，但是需要Node.js 的高级技术人员才能实现。有想投资参与的同学可以联系我（微信：chljapan）。注意不是普通的Nodejs开发者，需要高手才行。目前已经实现部分的建模功能，但是解析部分有一些问题。欢迎讨论。
 Webバージョンのモデル駆動プラートを開発する予定です。興味深い方がいらっしゃいましたら、ご連絡ください。ただNodeJsのハイレベルが必須です。
 

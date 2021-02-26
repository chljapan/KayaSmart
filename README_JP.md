# SmartKaYa
Metamodel, metadata-driven platform
# 使用
1. ダウンロードしてから、Mavenプロジェクトにコンバーターしてください。
   

2.　パスsrc/main/ResourcesにKayaのコンフィグファイルを新規作成してください。

	# Windowsの場合では
	
	#Log Path
	kayaMetaModelParseLogPath=C:/workspace/KayaModel.log
	
	# モデルファイル（バイナリーファイル：.mga  XML:.xme）
	
	#KayaModelFile path
	baseModel.basePath=C:/Users/LiangChen/Desktop/GME/
	
	# (':'前の部分はモデルバージョン、後ろはモデルファイル名です)
	
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
	
	# ランタイム方式 (dev:開発モード，バイナリーファイルmgaを利用する　 prod:デプロイモード，xmlファイルxmeを利用する，osシステムはWindowsの場合では両方サポートする、LINUX，Unix，MAC
	# の場合ではProdモードで利用してください)　
	
	# mode=dev
	mode=prod
	

    #Linux,Macos,Unix
    # ログファイル保存場所
    #Log Path
    kayaMetaModelParseLogPath=/Users/log/KayaModel.log

	baseModel.basePath=C:/Users/Chljapan/Desktop/GME/
	baseModel.mgaVersion=3.0:0001.mga
	baseModel.xmeVersion=3.0:0001.xme
   
    # ランタイム方式 (prod:デプロイモード，xmlファイルxmeを利用する，LINUX，Unix，MACの場合ではこのモードしか利用できない)　
    mode=prod

 3.　Windowsを利用して、開発を行う際、リアルタイムでモデルを確認したい場合（開発モード：dev）では下記の設定は必須です。
 
 JAUT.dllファイルを好きな場所にコピーし、システムPathに追加する
 
 Windowsは32ビットの場合ではファイルサイズは180KBのライブラリファイルを利用してください。
 Windowsは64ビットの場合ではファイルサイズは460KBのライブラリファイルを利用してください。
 
 4.　上記の配置でモデル駆動プラートフォームは完成です。
 
 5. KayaModelフォルダにある開発ガイドに従って、業務モデルの開発とランニングする
 
 PS　一部のパフォーマンス向上について、自分でも試してください。
 
	 #問い合わせ
          　ウェチャット：Chljapan　　Kayaをコメント入れ
    Email：chljapan@hotmail.com
    
    
 これからの予定：
 Webバージョンのモデル駆動プラートを開発する予定です。興味深い方がいらっしゃいましたら、ご連絡ください。ただNodeJsのハイレベルが必須です。
 

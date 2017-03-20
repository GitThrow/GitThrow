## application.propertiesの設定
jarの配置ディレクトリ/conf/application.propertiesを作成し、設定を書くことで設定を変更することが出来ます。

## 設定項目(主に使いそうなもの)

| プロパティ名 | 説明 |
|:-----------|------------:|
|server.port|サーバーのポート番号|
|spring.datasource.url|データベースのJDBC URL 今のところMySQLのみサポート|
|spring.datasource.driver-class-name|Driverの設定。MySQLを使う場合はcom.mysql.jdbc.Driver|
|spring.datasource.username|DB接続のためのユーザー名|
|spring.datasource.password|DB接続のためのパスワード|
|spring.http.multipart.max-file-size|1ファイルの最大バイト数|
|spring.http.multipart.max-request-size|1リクエストの最大バイト数|
|net.orekyuu.workbench.job-workspace-dir|ビルドやテストを走らせるためのディレクトリ|
|net.orekyuu.workbench.repository-dir|リモートリポジトリの配置ディレクトリ|
|net.orekyuu.workbench.artifact-dir|成果物を保存するディレクトリ|
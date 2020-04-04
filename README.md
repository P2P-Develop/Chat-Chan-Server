# Chat-Chan-Server
[![Build Status](https://travis-ci.org/P2P-Develop/Chat-Chan-Server.svg?branch=master)](https://travis-ci.org/P2P-Develop/Chat-Chan-Server)
[![Maintainability](https://api.codeclimate.com/v1/badges/4258b60be9426b44079f/maintainability)](https://codeclimate.com/github/P2P-Develop/Chat-Chan-Server/maintainability)
[![GitHub license](https://img.shields.io/github/license/P2P-Develop/Chat-Chan-Server)](https://github.com/P2P-Develop/Chat-Chan-Server/blob/master/LICENSE)
![Discord](https://img.shields.io/discord/688033631834603581)  
[Chat-Chan](https://github.com/P2P-Develop/Chat-Chan)のサーバーアプリケーションです。
## 概要
Chat-Chanをクライアントとして、サーバーを開くことができます。  
## 使い方
Chat-Chan-Serverのjarファイルがあるパスの場合、コンソールで以下のコマンドを入力することでサーバーを起動することができます。  
```console
$ java -jar Chat-ChanServer-[バージョン] 
```  
**注意: スタンドアロンコンソールはありません。直接コマンドプロンプトなどのプリインストールされたコンソールから起動してください。**
### コマンド
- help - コマンドのヘルプを表示します。
- stop - サーバーを停止します。
- kick [player] - 誰かが入室している場合のみ対象ユーザーをキックすることができます。
- list - 現在入室しているユーザーを表示します。

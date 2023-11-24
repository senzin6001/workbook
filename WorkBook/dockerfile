

# ビルドステージ
FROM gradle:7.3.3-jdk21 AS build

# 作業ディレクトリを設定
WORKDIR /app

# プロジェクトファイルを追加
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# ビルド実行
RUN gradle build --no-daemon

# 本番用ステージ
FROM adoptopenjdk:21-jre-hotspot-alpine3.15

# 作業ディレクトリを設定
WORKDIR /app

# ビルドステージからJARをコピー
COPY --from=build /app/build/libs/WorkBook-0.0.1-SNAPSHOT.jar demo.jar

# ポートをエクスポート
EXPOSE 8080

# アプリケーション起動
ENTRYPOINT ["java", "-jar", "demo.jar"]
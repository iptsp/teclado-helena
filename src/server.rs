use axum::routing::{get, Router};
use std::net::SocketAddr;

mod static_server;
mod websocket;

pub async fn serve() {
    let app = Router::new()
        .route("/", get(static_server::index_handler))
        .route("/index.html", get(static_server::index_handler))
        .route("/*file", get(static_server::static_handler))
        .route("/websocket", get(websocket::handler))
        .fallback_service(get(static_server::not_found));

    let addr = SocketAddr::from(([0, 0, 0, 0], 8080));
    println!("listening on {}", addr);
    let listener = tokio::net::TcpListener::bind(addr).await.unwrap();
    axum::serve(listener, app.into_make_service())
        .await
        .unwrap();
}

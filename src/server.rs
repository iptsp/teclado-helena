//! # Módulo de servidor de arquivos estáticos e WebSocket
//! 
//! O módulo `server` é responsável por configurar e iniciar um servidor
//! que serve tanto arquivos estáticos quanto suporta conexões WebSocket.
use axum::routing::{get, Router};
use std::net::SocketAddr;

mod static_server;
mod websocket;

/// Inicia um servidor HTTP que serve arquivos estáticos e lida com conexões WebSocket.
/// 
/// # Rotas
/// - `/`: Serve a página inicial fornecida pelo [`static_server::index_handler`].
/// - `/index.html`: Serve a mesma página que a rota `/`.
/// - `/*file`: Serve arquivos estáticos correspondentes ao caminho, manipulados por [`static_server::static_handler`].
/// - `/websocket`: Estabelece conexões WebSocket utilizando [`websocket::handler`].
/// - Outros caminhos não especificados acima irão acionar o [`static_server::not_found`] para responder com uma página de erro 404.
/// 
/// O servidor escuta em todas as interfaces de rede na porta 8080.
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

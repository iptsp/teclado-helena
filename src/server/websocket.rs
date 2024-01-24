//! Módulo responsável pelo gerenciamento de conexões WebSocket
//!
//! Este módulo provê funções para tratar requisições de upgrade do WebSocket
//! e para processar mensagens recebidas através do WebSocket.
use axum::{
    extract::{ws::WebSocket, WebSocketUpgrade},
    response::Response,
};

use crate::actions::*;

pub async fn handler(ws: WebSocketUpgrade) -> Response {
    ws.on_upgrade(handle_socket)
}

pub async fn handle_socket(mut socket: WebSocket) {
    while let Some(msg) = socket.recv().await {
        if let Ok(msg) = msg {
            let msgtext = msg.into_text().unwrap_or_default();
            if let Ok(action) = deserialize_action(&msgtext) {
                process_action(action);
            } else {
                println!("Invalid action");
            }
        };
    }
}

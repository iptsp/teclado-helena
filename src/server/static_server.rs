//! # Módulo responsável pelos arquivos estáticos
//!
//! Módulo responsável pelo gerenciamento de arquivos estáticos.
//!
//! Este módulo utiliza `axum` para a criação de handlers de rota e a biblioteca
//! `rust_embed` para embutir e servir arquivos estáticos.
use axum::{
    http::{header, StatusCode, Uri},
    response::{Html, IntoResponse, Response},
};
use rust_embed::RustEmbed;

/// Handler para a rota do índice (`/`).
///
/// Retorna o conteúdo do arquivo `index.html` ao ser chamado.
pub async fn index_handler() -> impl IntoResponse {
    static_handler("/index.html".parse::<Uri>().unwrap()).await
}

/// Handler genérico para servir arquivos estáticos.
///
/// Recebe o `Uri` da requisição e tenta servir o arquivo correspondente.
/// Se o arquivo não for encontrado, retorna `404 Not Found`.
///
/// # Argumentos
///
/// * `uri` - Um `Uri` contendo o caminho do arquivo a ser servido.
pub async fn static_handler(uri: Uri) -> impl IntoResponse {
    let path = uri.path().trim_start_matches('/').to_string();

    StaticFile(path)
}

/// Handler para a rota de Not Found (`404`).
///
/// Retorna uma página simples com o código de status `404` e a mensagem "Not Found".
pub async fn not_found() -> Html<&'static str> {
    Html("<h1>404</h1><p>Not Found</p>")
}

/// Estrutura para marcar uma pasta de arquivos estáticos com a macro `RustEmbed`.
///
/// A macro `RustEmbed` embute os arquivos estáticos localizados na pasta `client/dist`
/// para que possam ser acessados pelo binário compilado.
#[derive(RustEmbed)]
#[folder = "client/dist"]
struct Asset;

/// Estrutura representativa de um arquivo estático.
///
/// Permite a criação de uma resposta customizada implementando `IntoResponse`.
///
/// # Argumentos
///
/// * `T` - Um genérico que pode ser convertido em `String`, representando o caminho do arquivo.
pub struct StaticFile<T>(pub T);

/// Implementação de `IntoResponse` para `StaticFile`.
///
/// Permite que uma instância de `StaticFile` seja convertida em uma resposta HTTP,
/// que é então servida ao cliente.
///
/// Se o arquivo existir, é retornada uma resposta com o arquivo embutido e o MIME type correto.
/// Se o arquivo não existir, é retornada uma resposta com o código de status `404 Not Found`.
impl<T> IntoResponse for StaticFile<T>
where
    T: Into<String>,
{
    fn into_response(self) -> Response {
        let path = self.0.into();

        match Asset::get(path.as_str()) {
            Some(content) => {
                let mime = mime_guess::from_path(path).first_or_octet_stream();
                ([(header::CONTENT_TYPE, mime.as_ref())], content.data).into_response()
            }
            None => (StatusCode::NOT_FOUND, "404 Not Found").into_response(),
        }
    }
}

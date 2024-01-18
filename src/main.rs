#![windows_subsystem = "windows"]

use local_ip_address::local_ip;
use single_instance::SingleInstance;

mod actions;
mod server;
mod window;

#[tokio::main]
async fn main() {
    let instance_a = SingleInstance::new("Teclado Helena").unwrap();
    assert!(instance_a.is_single());
    
    let ip_url = get_ip_url();

    tokio::spawn(server::serve());
    window::create_window(ip_url);
}

fn get_ip_url() -> String {
    let my_local_ip = local_ip().unwrap();
    let ip_string = format!("http://{}:8080/", my_local_ip);
    ip_string
}

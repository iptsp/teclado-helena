#![windows_subsystem = "windows"]

use local_ip_address::local_ip;
use windows_sys::{
    w,
    Win32::{
        Foundation::{CloseHandle, GetLastError, HANDLE, ERROR_ALREADY_EXISTS},
        System::Threading::CreateMutexW,
    },
};

mod actions;
mod server;
mod window;

struct InstanceGuard(HANDLE);

impl Drop for InstanceGuard {
    fn drop(&mut self) {
        unsafe {
            CloseHandle(self.0);
        }
    }
}

fn acquire_single_instance() -> InstanceGuard {
    let handle = unsafe { CreateMutexW(std::ptr::null(), 0, w!("Teclado Helena")) };
    let error = unsafe { GetLastError() };

    assert!(
        !handle.is_null(),
        "failed to create the single-instance mutex"
    );
    let instance = InstanceGuard(handle);
    assert!(
        error != ERROR_ALREADY_EXISTS,
        "another Teclado Helena instance is already running"
    );
    instance
}

/// Ponto de entrada da aplicação
#[tokio::main]
async fn main() {
    let _instance = acquire_single_instance();

    let ip_url = get_ip_url();

    tokio::spawn(server::serve());
    window::create_window(ip_url);
}

/// Retorna uma URL contendo o endereço IP local da máquina e a porta padrão 8080.
///
/// # Exemplos
///
/// ```
/// let url = get_ip_url();
/// println!("{}", url); // A saída será algo como: "http://192.168.1.10:8080/"
/// ```
fn get_ip_url() -> String {
    let my_local_ip = local_ip().unwrap();
    let ip_string = format!("http://{}:8080/", my_local_ip);
    ip_string
}

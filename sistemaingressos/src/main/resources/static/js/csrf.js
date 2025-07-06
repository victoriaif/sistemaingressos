// static/js/csrf.js
let csrfHeader, csrfToken;

function atualizarCSRF() {
  csrfHeader = document.querySelector("input[name=_csrf_header]").value;
  csrfToken  = document.querySelector("input[name=_csrf]").value;
}

document.body.addEventListener("htmx:configRequest", (evt) => {
  if (evt.detail.verb !== "get") {
    atualizarCSRF();
    evt.detail.headers[csrfHeader] = csrfToken;
  }
});

//Juan Camilo Guerrero Diaz
const api = async (ruta, opciones = {}) => {
  const res = await fetch('/api' + ruta, { headers: { 'Content-Type': 'application/json' }, ...opciones });
  if (!res.ok) throw new Error('La API respondió con el código ' + res.status);
  return res.status === 204 ? null : res.json();
};

const esc = s => String(s ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));

function aviso(mensaje, tipo = 'success') {
  const el = document.createElement('div');
  el.className = `toast align-items-center text-bg-${tipo} border-0`;
  el.innerHTML = `<div class="d-flex"><div class="toast-body">${esc(mensaje)}</div>
    <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Cerrar"></button></div>`;
  document.getElementById('avisos').appendChild(el);
  el.addEventListener('hidden.bs.toast', () => el.remove());
  new bootstrap.Toast(el, { delay: 3500 }).show();
}

/* ---------- index.html: formularios ---------- */
async function cargarEquipos() {
  const equipos = await api('/equipos');
  const opciones = equipos.map(e => `<option value="${esc(e.id)}">${esc(e.nombre)}</option>`).join('');
  document.getElementById('mEquipos').innerHTML = opciones;
  document.getElementById('mCampeon').innerHTML = '<option value="">Aún sin campeón</option>' + opciones;
}

const formEquipo = document.getElementById('formEquipo');
if (formEquipo) {
  cargarEquipos().catch(e => aviso(e.message, 'danger'));

  formEquipo.addEventListener('submit', async ev => {
    ev.preventDefault();
    const d = Object.fromEntries(new FormData(formEquipo));
    try {
      await api('/equipos', {
        method: 'POST',
        body: JSON.stringify({ nombre: d.nombre, confederacion: d.confederacion, entrenador: d.entrenador, titulos: Number(d.titulos || 0) })
      });
      formEquipo.reset();
      aviso('Equipo guardado');
      cargarEquipos();
    } catch (e) { aviso(e.message, 'danger'); }
  });

  const formMundial = document.getElementById('formMundial');
  formMundial.addEventListener('submit', async ev => {
    ev.preventDefault();
    const d = Object.fromEntries(new FormData(formMundial));
    const campeon = document.getElementById('mCampeon').value;
    const equipos = Array.from(document.getElementById('mEquipos').selectedOptions).map(o => ({ id: o.value }));
    try {
      await api('/mundiales', {
        method: 'POST',
        body: JSON.stringify({ nombre: d.nombre, anio: Number(d.anio), sede: d.sede, campeon: campeon ? { id: campeon } : null, equipos })
      });
      formMundial.reset();
      aviso('Mundial guardado');
    } catch (e) { aviso(e.message, 'danger'); }
  });
}

/* ---------- listar.html: tablas ---------- */
async function cargarListado() {
  const [mundiales, equipos] = await Promise.all([api('/mundiales'), api('/equipos')]);
  mundiales.sort((a, b) => (a.anio ?? 0) - (b.anio ?? 0));

  document.getElementById('cntM').textContent = mundiales.length;
  document.getElementById('cntE').textContent = equipos.length;

  document.getElementById('tablaMundiales').innerHTML = mundiales.length
    ? mundiales.map(m => `<tr>
        <td class="fw-semibold">${esc(m.anio)}</td>
        <td>${esc(m.nombre)}</td>
        <td>${esc(m.sede)}</td>
        <td>${m.campeon ? `<span class="badge badge-gold"><i class="bi bi-trophy-fill"></i> ${esc(m.campeon.nombre)}</span>` : '<span class="text-secondary">Sin definir</span>'}</td>
        <td>${(m.equipos || []).filter(Boolean).map(e => `<span class="badge badge-team me-1">${esc(e.nombre)}</span>`).join('') || '<span class="text-secondary">Ninguno</span>'}</td>
        <td class="text-end"><button class="btn btn-sm btn-outline-danger" data-borrar="mundiales" data-id="${esc(m.id)}" aria-label="Eliminar mundial"><i class="bi bi-trash"></i></button></td>
      </tr>`).join('')
    : '<tr><td colspan="6" class="text-center text-secondary py-4">Aún no hay mundiales. <a href="/">Registra el primero</a>.</td></tr>';

  document.getElementById('tablaEquipos').innerHTML = equipos.length
    ? equipos.map(e => `<tr>
        <td class="fw-semibold">${esc(e.nombre)}</td>
        <td>${esc(e.confederacion)}</td>
        <td>${esc(e.entrenador)}</td>
        <td>${esc(e.titulos)}</td>
        <td class="text-end"><button class="btn btn-sm btn-outline-danger" data-borrar="equipos" data-id="${esc(e.id)}" aria-label="Eliminar equipo"><i class="bi bi-trash"></i></button></td>
      </tr>`).join('')
    : '<tr><td colspan="5" class="text-center text-secondary py-4">Aún no hay equipos. <a href="/">Registra el primero</a>.</td></tr>';
}

if (document.getElementById('tablaMundiales')) {
  cargarListado().catch(e => aviso(e.message, 'danger'));

  document.addEventListener('click', async ev => {
    const btn = ev.target.closest('[data-borrar]');
    if (!btn || !confirm('¿Eliminar este registro?')) return;
    try {
      await api(`/${btn.dataset.borrar}/${btn.dataset.id}`, { method: 'DELETE' });
      aviso('Registro eliminado');
      cargarListado();
    } catch (e) { aviso(e.message, 'danger'); }
  });
}

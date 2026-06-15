import axios from 'axios';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: { 'Content-Type': 'application/json' }
});

// ========== PROFISSIONAL DE SAÚDE (DEV 1 - Ana) ==========
export const profissionalService = {
  listar: (nome) => api.get('/profissionais', { params: nome ? { nome } : {} }),
  buscar: (id) => api.get(`/profissionais/${id}`),
  buscarPorCategoria: (categoria) => api.get(`/profissionais/categoria/${categoria}`),
  criar: (profissional) => api.post('/profissionais', profissional),
  atualizar: (id, profissional) => api.put(`/profissionais/${id}`, profissional),
  deletar: (id) => api.delete(`/profissionais/${id}`)
};

// ========== ATENDIMENTO (DEV 2 - Bruno) ==========
export const atendimentoService = {
  listar: () => api.get('/atendimentos'),
  buscar: (id) => api.get(`/atendimentos/${id}`),
  listarPorProfissional: (profissionalId) => api.get(`/atendimentos/profissional/${profissionalId}`),
  criar: (atendimento) => api.post('/atendimentos', atendimento),
  atualizar: (id, atendimento) => api.put(`/atendimentos/${id}`, atendimento),
  deletar: (id) => api.delete(`/atendimentos/${id}`)
};

export default api;

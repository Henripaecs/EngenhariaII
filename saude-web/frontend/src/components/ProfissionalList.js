import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { profissionalService } from '../services/api';

const BADGE_CLASS = {
  MEDICO: 'badge-medico',
  PSICOLOGO: 'badge-psicologo',
  FISIOTERAPEUTA: 'badge-fisioterapeuta'
};

const LABEL = {
  MEDICO: 'Médico',
  PSICOLOGO: 'Psicólogo',
  FISIOTERAPEUTA: 'Fisioterapeuta'
};

function ProfissionalList() {
  const [profissionais, setProfissionais] = useState([]);
  const [loading, setLoading] = useState(true);
  const [busca, setBusca] = useState('');
  const [filtroCategoria, setFiltroCategoria] = useState('');

  useEffect(() => {
    carregar();
  }, []);

  const carregar = async (nome = '') => {
    try {
      setLoading(true);
      const response = await profissionalService.listar(nome);
      setProfissionais(response.data);
    } catch (error) {
      console.error('Erro ao carregar profissionais:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleBusca = (e) => {
    e.preventDefault();
    if (filtroCategoria) {
      profissionalService.buscarPorCategoria(filtroCategoria)
        .then(r => setProfissionais(r.data))
        .catch(console.error);
    } else {
      carregar(busca);
    }
  };

  const deletar = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este profissional?')) {
      try {
        await profissionalService.deletar(id);
        carregar();
      } catch (error) {
        console.error('Erro ao deletar:', error);
      }
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>
      <div className="header">
        <h2>👨‍⚕️ Profissionais de Saúde</h2>
        <Link to="/profissionais/novo" className="btn btn-primary">+ Novo Profissional</Link>
      </div>

      <form onSubmit={handleBusca} style={{ display: 'flex', gap: '0.75rem', marginBottom: '1.2rem' }}>
        <input
          type="text"
          placeholder="Buscar por nome..."
          value={busca}
          onChange={e => { setBusca(e.target.value); setFiltroCategoria(''); }}
          style={{ padding: '0.5rem 0.8rem', borderRadius: 6, border: '1px solid #ccc', flex: 1 }}
        />
        <select
          value={filtroCategoria}
          onChange={e => { setFiltroCategoria(e.target.value); setBusca(''); }}
          style={{ padding: '0.5rem 0.8rem', borderRadius: 6, border: '1px solid #ccc' }}
        >
          <option value="">Todas as categorias</option>
          <option value="MEDICO">Médico</option>
          <option value="PSICOLOGO">Psicólogo</option>
          <option value="FISIOTERAPEUTA">Fisioterapeuta</option>
        </select>
        <button type="submit" className="btn btn-primary">Buscar</button>
        <button type="button" className="btn" onClick={() => { setBusca(''); setFiltroCategoria(''); carregar(); }}>
          Limpar
        </button>
      </form>

      <table className="table">
        <thead>
          <tr>
            <th>Nome</th>
            <th>Telefone</th>
            <th>Endereço</th>
            <th>Categorias</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {profissionais.map(prof => (
            <tr key={prof.id}>
              <td><strong>{prof.nome}</strong></td>
              <td>{prof.telefone || '-'}</td>
              <td>{prof.endereco || '-'}</td>
              <td>
                {(prof.categoria || []).map(cat => (
                  <span key={cat} className={`badge ${BADGE_CLASS[cat] || ''}`}>
                    {LABEL[cat] || cat}
                  </span>
                ))}
              </td>
              <td>
                <Link to={`/profissionais/editar/${prof.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => deletar(prof.id)} className="btn btn-danger btn-sm">Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {profissionais.length === 0 && <p className="empty">Nenhum profissional cadastrado.</p>}
    </div>
  );
}

export default ProfissionalList;

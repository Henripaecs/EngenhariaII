import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { atendimentoService } from '../services/api';

function AtendimentoList() {
  const [atendimentos, setAtendimentos] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    carregar();
  }, []);

  const carregar = async () => {
    try {
      const response = await atendimentoService.listar();
      setAtendimentos(response.data);
    } catch (error) {
      console.error('Erro ao carregar atendimentos:', error);
    } finally {
      setLoading(false);
    }
  };

  const deletar = async (id) => {
    if (window.confirm('Tem certeza que deseja excluir este atendimento?')) {
      try {
        await atendimentoService.deletar(id);
        carregar();
      } catch (error) {
        console.error('Erro ao deletar atendimento:', error);
      }
    }
  };

  if (loading) return <p>Carregando...</p>;

  return (
    <div>
      <div className="header">
        <h2>📋 Atendimentos</h2>
        <Link to="/atendimentos/novo" className="btn btn-primary">+ Novo Atendimento</Link>
      </div>

      <table className="table">
        <thead>
          <tr>
            <th>Data</th>
            <th>Horário</th>
            <th>Profissional</th>
            <th>Problema</th>
            <th>Receitas</th>
            <th>Ações</th>
          </tr>
        </thead>
        <tbody>
          {atendimentos.map(atend => (
            <tr key={atend.id}>
              <td>{atend.data}</td>
              <td>{atend.horario || '-'}</td>
              <td>{atend.profissional?.nome || '-'}</td>
              <td style={{ maxWidth: 200, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                {atend.problemaTexto || '-'}
              </td>
              <td>
                {(atend.receitaSaude || []).length > 0
                  ? <span style={{ fontSize: '0.82rem', color: '#1a6b3a' }}>
                      {atend.receitaSaude.length} receita(s)
                    </span>
                  : '-'}
              </td>
              <td>
                <Link to={`/atendimentos/editar/${atend.id}`} className="btn btn-sm">Editar</Link>
                <button onClick={() => deletar(atend.id)} className="btn btn-danger btn-sm">Excluir</button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      {atendimentos.length === 0 && <p className="empty">Nenhum atendimento registrado.</p>}
    </div>
  );
}

export default AtendimentoList;

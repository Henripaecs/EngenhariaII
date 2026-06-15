import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../services/api';

function AtendimentoForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [atendimento, setAtendimento] = useState({
    data: '', horario: '', problemaTexto: '', receitaSaude: [], profissional: null
  });
  const [profissionais, setProfissionais] = useState([]);
  const [novaReceita, setNovaReceita] = useState('');
  const [erro, setErro] = useState('');

  useEffect(() => {
    profissionalService.listar().then(res => setProfissionais(res.data));
    if (id) {
      atendimentoService.buscar(id).then(res => setAtendimento(res.data));
    }
  }, [id]);

  const adicionarReceita = () => {
    if (novaReceita.trim()) {
      setAtendimento({
        ...atendimento,
        receitaSaude: [...(atendimento.receitaSaude || []), novaReceita.trim()]
      });
      setNovaReceita('');
    }
  };

  const removerReceita = (index) => {
    const novas = atendimento.receitaSaude.filter((_, i) => i !== index);
    setAtendimento({ ...atendimento, receitaSaude: novas });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!atendimento.data) { setErro('Data é obrigatória.'); return; }
    try {
      if (id) {
        await atendimentoService.atualizar(id, atendimento);
      } else {
        await atendimentoService.criar(atendimento);
      }
      navigate('/atendimentos');
    } catch (error) {
      setErro('Erro ao salvar atendimento.');
      console.error(error);
    }
  };

  // Mostra receitas sugeridas com base no tipo do profissional selecionado
  const profissionalSelecionado = profissionais.find(
    p => p.id === atendimento.profissional?.id
  );
  const sugestoes = profissionalSelecionado?.categoria?.map(cat => {
    if (cat === 'MEDICO') return 'Remédio: ';
    if (cat === 'FISIOTERAPEUTA') return 'Atividade Física: ';
    if (cat === 'PSICOLOGO') return 'Atividade Mental: ';
    return '';
  }) || [];

  return (
    <div>
      <h2 style={{ marginBottom: '1.5rem', color: '#1a6b3a' }}>
        {id ? '✏️ Editar Atendimento' : '➕ Novo Atendimento'}
      </h2>

      <div className="form" style={{ maxWidth: 700 }}>
        {erro && <div className="alert-error">{erro}</div>}

        <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
          <div className="form-group">
            <label>Data *</label>
            <input
              type="date"
              value={atendimento.data || ''}
              required
              onChange={e => setAtendimento({ ...atendimento, data: e.target.value })}
            />
          </div>
          <div className="form-group">
            <label>Horário</label>
            <input
              type="time"
              value={atendimento.horario || ''}
              onChange={e => setAtendimento({ ...atendimento, horario: e.target.value })}
            />
          </div>
        </div>

        <div className="form-group">
          <label>Profissional de Saúde</label>
          <select
            value={atendimento.profissional?.id || ''}
            onChange={e => setAtendimento({
              ...atendimento,
              profissional: e.target.value ? { id: parseInt(e.target.value) } : null
            })}
          >
            <option value="">Selecione um profissional</option>
            {profissionais.map(p => (
              <option key={p.id} value={p.id}>
                {p.nome} ({(p.categoria || []).join(', ')})
              </option>
            ))}
          </select>
        </div>

        <div className="form-group">
          <label>Problema / Queixa do Paciente</label>
          <textarea
            value={atendimento.problemaTexto || ''}
            placeholder="Descreva o problema ou queixa relatada pelo paciente..."
            onChange={e => setAtendimento({ ...atendimento, problemaTexto: e.target.value })}
          />
        </div>

        <div className="form-group">
          <label>
            Receitas de Saúde
            {sugestoes.length > 0 && (
              <small style={{ color: '#888', marginLeft: 8 }}>
                Sugestão de prefixo: {sugestoes.join(' | ')}
              </small>
            )}
          </label>

          {(atendimento.receitaSaude || []).map((r, i) => (
            <div key={i} style={{ display: 'flex', gap: '0.5rem', marginBottom: '0.4rem', alignItems: 'center' }}>
              <span style={{
                flex: 1, background: '#f0fff4', border: '1px solid #b2dfdb',
                borderRadius: 6, padding: '0.4rem 0.8rem', fontSize: '0.9rem'
              }}>
                {r}
              </span>
              <button type="button" className="btn btn-danger btn-sm" onClick={() => removerReceita(i)}>✕</button>
            </div>
          ))}

          <div style={{ display: 'flex', gap: '0.5rem', marginTop: '0.5rem' }}>
            <input
              type="text"
              value={novaReceita}
              placeholder={sugestoes[0] || 'Ex: Paracetamol 500mg - 1 comprimido de 8h em 8h'}
              onChange={e => setNovaReceita(e.target.value)}
              onKeyDown={e => e.key === 'Enter' && (e.preventDefault(), adicionarReceita())}
              style={{ flex: 1, padding: '0.5rem 0.8rem', borderRadius: 6, border: '1px solid #ccc' }}
            />
            <button type="button" className="btn btn-primary" onClick={adicionarReceita}>+ Adicionar</button>
          </div>
        </div>

        <div className="form-actions">
          <button type="button" className="btn btn-primary" onClick={handleSubmit}>💾 Salvar</button>
          <button type="button" className="btn" onClick={() => navigate('/atendimentos')}>Cancelar</button>
        </div>
      </div>
    </div>
  );
}

export default AtendimentoForm;

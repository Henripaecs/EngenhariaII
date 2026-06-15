import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { profissionalService } from '../services/api';

const CATEGORIAS = [
  { value: 'MEDICO', label: 'Médico' },
  { value: 'PSICOLOGO', label: 'Psicólogo' },
  { value: 'FISIOTERAPEUTA', label: 'Fisioterapeuta' }
];

function ProfissionalForm() {
  const navigate = useNavigate();
  const { id } = useParams();
  const [profissional, setProfissional] = useState({
    nome: '', telefone: '', endereco: '', categoria: []
  });
  const [erro, setErro] = useState('');

  useEffect(() => {
    if (id) {
      profissionalService.buscar(id)
        .then(res => setProfissional(res.data))
        .catch(() => setErro('Profissional não encontrado.'));
    }
  }, [id]);

  const toggleCategoria = (valor) => {
    const atual = profissional.categoria || [];
    const nova = atual.includes(valor)
      ? atual.filter(c => c !== valor)
      : [...atual, valor];
    setProfissional({ ...profissional, categoria: nova });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!profissional.categoria || profissional.categoria.length === 0) {
      setErro('Selecione pelo menos uma categoria.');
      return;
    }
    try {
      if (id) {
        await profissionalService.atualizar(id, profissional);
      } else {
        await profissionalService.criar(profissional);
      }
      navigate('/profissionais');
    } catch (error) {
      setErro('Erro ao salvar profissional. Verifique os dados.');
      console.error(error);
    }
  };

  return (
    <div>
      <h2 style={{ marginBottom: '1.5rem', color: '#1a6b3a' }}>
        {id ? '✏️ Editar Profissional' : '➕ Novo Profissional de Saúde'}
      </h2>

      <div className="form">
        {erro && <div className="alert-error">{erro}</div>}

        <div className="form-group">
          <label>Nome *</label>
          <input
            type="text"
            value={profissional.nome}
            required
            placeholder="Ex: Dr. João Silva"
            onChange={e => setProfissional({ ...profissional, nome: e.target.value })}
          />
        </div>

        <div className="form-group">
          <label>Telefone</label>
          <input
            type="text"
            value={profissional.telefone || ''}
            placeholder="Ex: (11) 99999-0000"
            onChange={e => setProfissional({ ...profissional, telefone: e.target.value })}
          />
        </div>

        <div className="form-group">
          <label>Endereço</label>
          <input
            type="text"
            value={profissional.endereco || ''}
            placeholder="Ex: Rua das Flores, 100 - São Paulo/SP"
            onChange={e => setProfissional({ ...profissional, endereco: e.target.value })}
          />
        </div>

        <div className="form-group">
          <label>Categorias * <small style={{ color: '#888' }}>(selecione uma ou mais)</small></label>
          <div className="checkboxes">
            {CATEGORIAS.map(cat => (
              <label key={cat.value} className="checkbox-item">
                <input
                  type="checkbox"
                  checked={(profissional.categoria || []).includes(cat.value)}
                  onChange={() => toggleCategoria(cat.value)}
                />
                {cat.label}
              </label>
            ))}
          </div>
        </div>

        <div className="form-actions">
          <button type="button" className="btn btn-primary" onClick={handleSubmit}>
            💾 Salvar
          </button>
          <button type="button" className="btn" onClick={() => navigate('/profissionais')}>
            Cancelar
          </button>
        </div>
      </div>
    </div>
  );
}

export default ProfissionalForm;

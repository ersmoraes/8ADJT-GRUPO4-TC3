INSERT IGNORE INTO usuarios (id, username, password, nome, email, tipo_usuario, ativo) VALUES
(1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeVUc3yG3fh3BjNXPHBvfaQVwvHYeIxNu', 'Administrador', 'admin@hospital.com', 'MEDICO', true),
(2, 'dr.silva', '$2a$10$N9qo8uLOickgx2ZMRZoMyeVUc3yG3fh3BjNXPHBvfaQVwvHYeIxNu', 'Dr. João Silva', 'joao.silva@hospital.com', 'MEDICO', true),
(3, 'dra.santos', '$2a$10$N9qo8uLOickgx2ZMRZoMyeVUc3yG3fh3BjNXPHBvfaQVwvHYeIxNu', 'Dra. Maria Santos', 'maria.santos@hospital.com', 'MEDICO', true),
(4, 'enf.ana', '$2a$10$N9qo8uLOickgx2ZMRZoMyeVUc3yG3fh3BjNXPHBvfaQVwvHYeIxNu', 'Ana Enfermeira', 'ana.enfermeira@hospital.com', 'ENFERMEIRO', true),
(5, 'enf.carlos', '$2a$10$N9qo8uLOickgx2ZMRZoMyeVUc3yG3fh3BjNXPHBvfaQVwvHYeIxNu', 'Carlos Enfermeiro', 'carlos.enfermeiro@hospital.com', 'ENFERMEIRO', true),
(6, 'paciente1', '$2a$10$N9qo8uLOickgx2ZMRZoMyeVUc3yG3fh3BjNXPHBvfaQVwvHYeIxNu', 'Pedro Paciente', 'pedro.paciente@email.com', 'PACIENTE', true),
(7, 'paciente2', '$2a$10$N9qo8uLOickgx2ZMRZoMyeVUc3yG3fh3BjNXPHBvfaQVwvHYeIxNu', 'Julia Paciente', 'julia.paciente@email.com', 'PACIENTE', true);

-- Senha para todos os usuários: "password123"
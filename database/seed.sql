-- CRM Jurídico - sample data (100% fictitious).
-- Every name, NIF, phone, e-mail and case number below is invented.
-- Run against a NEW/EMPTY database: explicit IDs and UNIQUE columns will
-- clash with existing rows. See README.md ("Option B – Sample data").
--
-- Notes:
--  * All users share the password "1234" (stored as its SHA-256 hash).
--  * NIF/phone columns are INTEGER, so leading zeros (000000001) are stored as 1.
--  * Enum columns hold the Java enum names; dates use ISO-8601 text.

PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS utilizador(
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    username TEXT UNIQUE NOT NULL,
    password TEXT NOT NULL,
    nif INTEGER UNIQUE,
    dataAdmissao TEXT,
    dataDemissao TEXT,
    nivelAcesso TEXT,
    ativo INTEGER DEFAULT 1,
    cedulaProfissional TEXT
);
CREATE TABLE IF NOT EXISTS cliente (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nome TEXT NOT NULL,
    morada TEXT,
    telefone INTEGER,
    email TEXT,
    nif INTEGER UNIQUE,
    tipoCliente TEXT,
    dataCadastro TEXT,
    ativo INTEGER DEFAULT 1
);
CREATE TABLE IF NOT EXISTS processo (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    numProcesso TEXT UNIQUE,
    nomeAcao TEXT,
    estado TEXT,
    dataDistribuicao TEXT,
    dataFinalizacao TEXT,
    id_advogado INTEGER,
    id_cliente INTEGER,
    FOREIGN KEY (id_advogado) REFERENCES utilizador(id),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);
CREATE TABLE IF NOT EXISTS tarefa (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    descricao TEXT,
    prioridade TEXT,
    estado TEXT,
    dataLimite TEXT,
    id_processo INTEGER,
    id_utilizador INTEGER,
    FOREIGN KEY (id_processo) REFERENCES processo(id),
    FOREIGN KEY (id_utilizador) REFERENCES utilizador(id)
);
CREATE TABLE IF NOT EXISTS agenda (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    local TEXT,
    dataProtocolo TEXT,
    id_processo INTEGER,
    id_utilizador INTEGER,
    estado TEXT DEFAULT 'AGENDADO',
    FOREIGN KEY (id_processo) REFERENCES processo(id),
    FOREIGN KEY (id_utilizador) REFERENCES utilizador(id)
);

BEGIN TRANSACTION;

-- Users (demo password for all: 1234, hashed with Hash.sha256 from the project:
-- unsalted SHA-256, lowercase hex. Demo admin: admin / 1234)
INSERT INTO utilizador (id, nome, username, password, nif, dataAdmissao, dataDemissao, nivelAcesso, ativo, cedulaProfissional) VALUES
  (1, 'Administrador Exemplo', 'admin', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 000000101, '2024-01-01', NULL, 'ADMINISTRADOR', 1, NULL),
  (2, 'Advogado Exemplo 01', 'advogado01', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 000000102, '2024-02-01', NULL, 'ADVOGADO', 1, 'EXEMPLO-001'),
  (3, 'Advogado Exemplo 02', 'advogado02', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 000000103, '2024-03-01', NULL, 'ADVOGADO', 1, 'EXEMPLO-002'),
  (4, 'Estagiario Exemplo 01', 'estagiario01', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 000000104, '2025-01-15', NULL, 'ESTAGIARIO', 1, NULL),
  (5, 'Estagiario Exemplo 02 (inativo)', 'estagiario02', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 000000105, '2024-06-01', '2025-06-30', 'ESTAGIARIO', 0, NULL);

-- Clients
INSERT INTO cliente (id, nome, morada, telefone, email, nif, tipoCliente, dataCadastro, ativo) VALUES
  (1, 'Cliente Exemplo 01', 'Rua Exemplo, 1', 900000001, 'cliente01@exemplo.com', 000000001, 'SINGULAR', '2025-02-10', 1),
  (2, 'Cliente Exemplo 02', 'Rua Exemplo, 2', 900000002, 'cliente02@exemplo.com', 000000002, 'SINGULAR', '2025-03-10', 1),
  (3, 'Empresa Exemplo 03 Lda', 'Rua Exemplo, 3', 900000003, 'cliente03@exemplo.com', 000000003, 'EMPRESA', '2025-04-10', 1),
  (4, 'Cliente Exemplo 04', 'Rua Exemplo, 4', 900000004, 'cliente04@exemplo.com', 000000004, 'SINGULAR', '2025-05-10', 1),
  (5, 'Cliente Exemplo 05', 'Rua Exemplo, 5', 900000005, 'cliente05@exemplo.com', 000000005, 'SINGULAR', '2025-06-10', 1),
  (6, 'Empresa Exemplo 06 Lda', 'Rua Exemplo, 6', 900000006, 'cliente06@exemplo.com', 000000006, 'EMPRESA', '2025-07-10', 1),
  (7, 'Cliente Exemplo 07', 'Rua Exemplo, 7', 900000007, 'cliente07@exemplo.com', 000000007, 'SINGULAR', '2025-08-10', 1),
  (8, 'Cliente Exemplo 08', 'Rua Exemplo, 8', 900000008, 'cliente08@exemplo.com', 000000008, 'SINGULAR', '2025-09-10', 0);

-- Cases
INSERT INTO processo (id, numProcesso, nomeAcao, estado, dataDistribuicao, dataFinalizacao, id_advogado, id_cliente) VALUES
  (1, 'EXEMPLO-0001/2025', 'Acao de Exemplo B 01', 'DISTRIBUIDO', '2025-02-15', NULL, 3, 1),
  (2, 'EXEMPLO-0002/2025', 'Acao de Exemplo C 02', 'EM_CURSO', '2025-03-15', NULL, 2, 2),
  (3, 'EXEMPLO-0003/2025', 'Acao de Exemplo A 03', 'EM_CURSO', '2025-04-15', NULL, 3, 3),
  (4, 'EXEMPLO-0004/2025', 'Acao de Exemplo B 04', 'SUSPENSO', '2025-05-15', NULL, 2, 4),
  (5, 'EXEMPLO-0005/2025', 'Acao de Exemplo C 05', 'FINALIZADO', '2025-06-15', '2025-12-05', 3, 5),
  (6, 'EXEMPLO-0006/2025', 'Acao de Exemplo A 06', 'ARQUIVADO', '2025-07-15', '2025-12-06', 2, 6),
  (7, 'EXEMPLO-0007/2025', 'Acao de Exemplo B 07', 'EM_CURSO', '2025-08-15', NULL, 3, 7),
  (8, 'EXEMPLO-0008/2025', 'Acao de Exemplo C 08', 'DISTRIBUIDO', '2025-09-15', NULL, 2, 8);

-- Tasks
INSERT INTO tarefa (id, titulo, descricao, prioridade, estado, dataLimite, id_processo, id_utilizador) VALUES
  (1, 'Tarefa de Exemplo 01', 'Descricao ficticia da tarefa 01.', 'MEDIA', 'EM_CURSO', '2026-02-20', 2, 3),
  (2, 'Tarefa de Exemplo 02', 'Descricao ficticia da tarefa 02.', 'ALTA', 'CONCLUIDA', '2026-03-20', 3, 4),
  (3, 'Tarefa de Exemplo 03', 'Descricao ficticia da tarefa 03.', 'URGENTE', 'CANCELADA', '2026-04-20', 4, 2),
  (4, 'Tarefa de Exemplo 04', 'Descricao ficticia da tarefa 04.', 'BAIXA', 'PENDENTE', '2026-05-20', 5, 3),
  (5, 'Tarefa de Exemplo 05', 'Descricao ficticia da tarefa 05.', 'MEDIA', 'EM_CURSO', '2026-06-20', 6, 4),
  (6, 'Tarefa de Exemplo 06', 'Descricao ficticia da tarefa 06.', 'ALTA', 'CONCLUIDA', '2026-07-20', 7, 2),
  (7, 'Tarefa de Exemplo 07', 'Descricao ficticia da tarefa 07.', 'URGENTE', 'CANCELADA', '2026-08-20', 8, 3),
  (8, 'Tarefa de Exemplo 08', 'Descricao ficticia da tarefa 08.', 'BAIXA', 'PENDENTE', '2026-09-20', 1, 4),
  (9, 'Tarefa de Exemplo 09', 'Descricao ficticia da tarefa 09.', 'MEDIA', 'EM_CURSO', '2026-01-20', 2, 2),
  (10, 'Tarefa de Exemplo 10', 'Descricao ficticia da tarefa 10.', 'ALTA', 'CONCLUIDA', '2026-02-20', 3, 3);

-- Agenda
INSERT INTO agenda (id, titulo, local, dataProtocolo, id_processo, id_utilizador, estado) VALUES
  (1, 'Audiencia de Exemplo 01', 'Tribunal Exemplo 2', '2026-02-11T11:30', 1, 3, 'AGENDADO'),
  (2, 'Audiencia de Exemplo 02', 'Tribunal Exemplo 3', '2026-03-12T12:30', 2, 2, 'CONCLUIDO'),
  (3, 'Audiencia de Exemplo 03', 'Tribunal Exemplo 1', '2026-04-13T13:30', 3, 3, 'CANCELADO'),
  (4, 'Audiencia de Exemplo 04', 'Tribunal Exemplo 2', '2026-05-14T14:30', 4, 2, 'AGENDADO'),
  (5, 'Audiencia de Exemplo 05', 'Tribunal Exemplo 3', '2026-06-15T15:30', 5, 3, 'AGENDADO'),
  (6, 'Audiencia de Exemplo 06', 'Tribunal Exemplo 1', '2026-07-16T16:30', 6, 2, 'CONCLUIDO'),
  (7, 'Audiencia de Exemplo 07', 'Tribunal Exemplo 2', '2026-08-17T17:30', 7, 3, 'CANCELADO'),
  (8, 'Audiencia de Exemplo 08', 'Tribunal Exemplo 3', '2026-09-18T18:30', 8, 2, 'AGENDADO');

COMMIT;

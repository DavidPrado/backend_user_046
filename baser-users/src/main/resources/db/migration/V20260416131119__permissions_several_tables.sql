INSERT INTO permissions (id, name, description) VALUES
(gen_random_uuid(), 'ALUNO_CREATE', 'Permite criar novos alunos'),
(gen_random_uuid(), 'ALUNO_UPDATE', 'Permite atualizar dados de alunos'),
(gen_random_uuid(), 'ALUNO_READ', 'Permite visualizar dados de alunos'),
(gen_random_uuid(), 'ALUNO_DELETE', 'Permite excluir alunos'),

(gen_random_uuid(), 'CARGO_EMPRESA_CREATE', 'Permite criar cargos de empresa'),
(gen_random_uuid(), 'CARGO_EMPRESA_UPDATE', 'Permite atualizar cargos de empresa'),
(gen_random_uuid(), 'CARGO_EMPRESA_READ', 'Permite visualizar cargos de empresa'),
(gen_random_uuid(), 'CARGO_EMPRESA_DELETE', 'Permite excluir cargos de empresa'),

(gen_random_uuid(), 'CURSO_ALUNO_CREATE', 'Permite vincular aluno a curso'),
(gen_random_uuid(), 'CURSO_ALUNO_UPDATE', 'Permite atualizar vínculo de aluno e curso'),
(gen_random_uuid(), 'CURSO_ALUNO_READ', 'Permite visualizar vínculos de alunos e cursos'),
(gen_random_uuid(), 'CURSO_ALUNO_DELETE', 'Permite excluir vínculo de aluno e curso'),

(gen_random_uuid(), 'CURSO_CREATE', 'Permite criar novos cursos'),
(gen_random_uuid(), 'CURSO_UPDATE', 'Permite atualizar cursos'),
(gen_random_uuid(), 'CURSO_READ', 'Permite visualizar cursos'),
(gen_random_uuid(), 'CURSO_DELETE', 'Permite excluir cursos'),

(gen_random_uuid(), 'EMPRESA_CREATE', 'Permite cadastrar empresas'),
(gen_random_uuid(), 'EMPRESA_UPDATE', 'Permite atualizar empresas'),
(gen_random_uuid(), 'EMPRESA_READ', 'Permite visualizar empresas'),
(gen_random_uuid(), 'EMPRESA_DELETE', 'Permite excluir empresas'),

(gen_random_uuid(), 'ESCOLA_CREATE', 'Permite cadastrar escolas'),
(gen_random_uuid(), 'ESCOLA_UPDATE', 'Permite atualizar escolas'),
(gen_random_uuid(), 'ESCOLA_READ', 'Permite visualizar escolas'),
(gen_random_uuid(), 'ESCOLA_DELETE', 'Permite excluir escolas'),

(gen_random_uuid(), 'ESTAGIO_CREATE', 'Permite criar estágios'),
(gen_random_uuid(), 'ESTAGIO_UPDATE', 'Permite atualizar estágios'),
(gen_random_uuid(), 'ESTAGIO_READ', 'Permite visualizar estágios'),
(gen_random_uuid(), 'ESTAGIO_DELETE', 'Permite excluir estágios'),

(gen_random_uuid(), 'ESTAGIO_ARQUIVO_READ', 'Permite visualizar arquivos de estágio'),
(gen_random_uuid(), 'ESTAGIO_ARQUIVO_UPDATE', 'Permite atualizar arquivos de estágio'),
(gen_random_uuid(), 'ESTAGIO_ARQUIVO_DELETE', 'Permite excluir arquivos de estágio'),

(gen_random_uuid(), 'FUNCIONARIO_CREATE', 'Permite cadastrar funcionários'),
(gen_random_uuid(), 'FUNCIONARIO_UPDATE', 'Permite atualizar funcionários'),
(gen_random_uuid(), 'FUNCIONARIO_READ', 'Permite visualizar funcionários'),
(gen_random_uuid(), 'FUNCIONARIO_DELETE', 'Permite excluir funcionários'),

(gen_random_uuid(), 'MATRICULA_CREATE', 'Permite criar matrículas'),

(gen_random_uuid(), 'RESPONSAVEL_ALUNO_CREATE', 'Permite cadastrar responsáveis de alunos'),
(gen_random_uuid(), 'RESPONSAVEL_ALUNO_UPDATE', 'Permite atualizar responsáveis de alunos'),
(gen_random_uuid(), 'RESPONSAVEL_ALUNO_READ', 'Permite visualizar responsáveis de alunos'),
(gen_random_uuid(), 'RESPONSAVEL_ALUNO_DELETE', 'Permite excluir responsáveis de alunos');

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r, permissions p
WHERE r.name = 'ROLE_ADMIN'
AND p.name IN (
    'ALUNO_CREATE', 'ALUNO_UPDATE', 'ALUNO_READ', 'ALUNO_DELETE',
    'CARGO_EMPRESA_CREATE', 'CARGO_EMPRESA_UPDATE', 'CARGO_EMPRESA_READ', 'CARGO_EMPRESA_DELETE',
    'CURSO_ALUNO_CREATE', 'CURSO_ALUNO_UPDATE', 'CURSO_ALUNO_READ', 'CURSO_ALUNO_DELETE',
    'CURSO_CREATE', 'CURSO_UPDATE', 'CURSO_READ', 'CURSO_DELETE',
    'EMPRESA_CREATE', 'EMPRESA_UPDATE', 'EMPRESA_READ', 'EMPRESA_DELETE',
    'ESCOLA_CREATE', 'ESCOLA_UPDATE', 'ESCOLA_READ', 'ESCOLA_DELETE',
    'ESTAGIO_CREATE', 'ESTAGIO_UPDATE', 'ESTAGIO_READ', 'ESTAGIO_DELETE',
    'ESTAGIO_ARQUIVO_READ', 'ESTAGIO_ARQUIVO_UPDATE', 'ESTAGIO_ARQUIVO_DELETE',
    'FUNCIONARIO_CREATE', 'FUNCIONARIO_UPDATE', 'FUNCIONARIO_READ', 'FUNCIONARIO_DELETE',
    'MATRICULA_CREATE',
    'RESPONSAVEL_ALUNO_CREATE', 'RESPONSAVEL_ALUNO_UPDATE', 'RESPONSAVEL_ALUNO_READ', 'RESPONSAVEL_ALUNO_DELETE'
);
ALTER TABLE enterprise
ADD CONSTRAINT uk_empresa_cnpj UNIQUE (cnpj);
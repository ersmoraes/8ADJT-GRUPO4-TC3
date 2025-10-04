CREATE DATABASE IF NOT EXISTS hospital_agendamento;
CREATE DATABASE IF NOT EXISTS hospital_historico;
CREATE DATABASE IF NOT EXISTS hospital_notificacao;

-- Concede todas as permissões ao usuário root
GRANT ALL PRIVILEGES ON hospital_agendamento.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON hospital_historico.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON hospital_notificacao.* TO 'root'@'%';
FLUSH PRIVILEGES;

USE hospital_agendamento;

-- Tabela de usuários será criada automaticamente pelo Hibernate
INSERT IGNORE INTO usuarios (id, username, password, nome, email, tipo_usuario, ativo) VALUES
(1, 'admin', '$2a$10$peSvzcB4G3jRfjT21tBrDuyBJV8kbh6qtk.JCH6isfQUqjToivnae', 'Administrador', 'admin@hospital.com', 'MEDICO', true),
(2, 'dr.silva', '$2a$10$peSvzcB4G3jRfjT21tBrDuyBJV8kbh6qtk.JCH6isfQUqjToivnae', 'Dr. João Silva', 'joao.silva@hospital.com', 'MEDICO', true),
(3, 'dra.santos', '$2a$10$peSvzcB4G3jRfjT21tBrDuyBJV8kbh6qtk.JCH6isfQUqjToivnae', 'Dra. Maria Santos', 'maria.santos@hospital.com', 'MEDICO', true),
(4, 'enf.ana', '$2a$10$peSvzcB4G3jRfjT21tBrDuyBJV8kbh6qtk.JCH6isfQUqjToivnae', 'Ana Enfermeira', 'ana.enfermeira@hospital.com', 'ENFERMEIRO', true),
(5, 'enf.carlos', '$2a$10$peSvzcB4G3jRfjT21tBrDuyBJV8kbh6qtk.JCH6isfQUqjToivnae', 'Carlos Enfermeiro', 'carlos.enfermeiro@hospital.com', 'ENFERMEIRO', true),
(6, 'paciente1', '$2a$10$peSvzcB4G3jRfjT21tBrDuyBJV8kbh6qtk.JCH6isfQUqjToivnae', 'Pedro Paciente', 'pedro.paciente@email.com', 'PACIENTE', true),
(7, 'paciente2', '$2a$10$peSvzcB4G3jRfjT21tBrDuyBJV8kbh6qtk.JCH6isfQUqjToivnae', 'Julia Paciente', 'julia.paciente@email.com', 'PACIENTE', true);

---

# .gitignore
# Compiled class file
*.class

# Log file
*.log

# BlueJ files
*.ctxt

# Mobile Tools for Java (J2ME)
.mtj.tmp/

# Package Files #
*.jar
*.war
*.nar
*.ear
*.zip
*.tar.gz
*.rar

# virtual machine crash logs
hs_err_pid*

# Maven
target/
pom.xml.tag
pom.xml.releaseBackup
pom.xml.versionsBackup
pom.xml.next
release.properties
dependency-reduced-pom.xml
buildNumber.properties
.mvn/timing.properties
.mvn/wrapper/maven-wrapper.jar

# IDE
.idea/
*.iws
*.iml
*.ipr
.vscode/
.classpath
.project
.settings/
bin/

# OS
.DS_Store
.DS_Store?
._*
.Spotlight-V100
.Trashes
ehthumbs.db
Thumbs.db

# Spring Boot
application-local.yml
application-dev.yml

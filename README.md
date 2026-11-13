![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/gabrielborel/devops/ci.yml?branch=main)

# DevOps CRUD

Projeto base utilizado na avaliação de DevOps.

A aplicação disponibiliza uma API REST simples para cadastro de produtos utilizando Spring Boot, Spring Data JPA e banco H2 em memória.

## Requisitos

- Java 17 ou superior
- Maven 3.6.3 ou superior

## Como executar a aplicação

```bash
# roda os testes
mvn test

# compila, roda os testes e gera o jar em target/
mvn package

# sobe a aplicação a partir do jar gerado, de acordo com a versão
java -jar target/devops-crud-1.0.0.jar
```
## Workflows

- **`ci.yml`**: integração contínua. Valida e compila, roda os testes, empacota o jar como artefato e gera um resumo da execução.
- **`run-monitor.yml`**: demonstra configuração e diagnóstico. Valida variáveis e secrets, emite avisos e erros, simula falhas e faz o diagnóstico automático.
- **`deploy.yml`**: entrega em produção. Reaproveita o CI e só faz o deploy simulado depois da aprovação manual no environment `production`.## Git e CI/CD

### Git
O Git é o sistema de controle de versão e a única fonte de verdade do código-fonte, garantindo a rastreabilidade: o histórico de quem mudou o quê e quando. Ele não dispara nada sozinho. Quem inicia os workflows é o GitHub, ao receber um push ou um Pull Request, e é a partir daí que as mudanças começam a ser testadas e implantadas.

### Branches
Branches permitem realizar diversas tarefas de forma isolada, sem afetar diretamente o código que é executado em produção. Elas se conectam por meio de ações controladas de **merge**. O **Pull Request** atua como o ponto de controle: o pipeline roda nele, antes do merge, garantindo a qualidade do código. Isso é diferente de trabalhar direto na `main`, onde o problema só aparece depois de já estar na branch principal. O teste que quebrei de propósito serve de exemplo: o pipeline falhou no PR, antes de qualquer merge.

### Tags
Tag é um marcador estático que aponta para um commit específico, geralmente utilizado para marcar versões. Por convenção ela é imutável: tecnicamente dá para mover uma tag, mas não se faz. Já a branch é uma ramificação do código para realizar tarefas, testes e correções sem afetar o código principal. A diferença central é que a branch avança a cada novo commit, enquanto a tag fica parada no commit que foi marcado.

### Releases
A **versão** é um identificador do estado do software, no formato `v1.0.0` (major.minor.patch), em que o número muda conforme o tipo de mudança: uma correção, uma funcionalidade nova ou uma quebra de compatibilidade. A **tag** é o marcador que aponta para o commit daquela versão e congela o código naquele ponto, o que permite voltar ao código exato que gerou a `v1.0.0`. A **release** é a camada de apresentação e distribuição que a plataforma de hospedagem constrói em cima de uma determinada tag, e pode trazer título, descrição, changelog (o que mudou naquela versão) e arquivos do projeto compilados, anexados.

### Workflows
São fluxos de trabalho automatizados e configuráveis, disparados por eventos, como um push ou a abertura de um Pull Request. Um workflow é composto por um ou mais **jobs**, e cada job por **steps**. Por padrão, os jobs rodam em paralelo, e quando um depende de outro a ordem é definida com o `needs`. Eles realizam diversas ações para a entrega de software de forma contínua dentro de um processo de CI/CD, como rodar os testes antes de liberar o deploy.

### Variáveis e Secrets
**env** são variáveis de ambiente, dados estáticos ou dinâmicos definidos no arquivo do workflow, no nível do workflow, de um job ou de um step. Ficam no YAML, junto com o código, e devem ser utilizadas para valores não secretos, como o `STAGE` que uso nos meus workflows. Também podem repassar um secret para o shell, como no `run-monitor` (`API_KEY: ${{ secrets.API_KEY }}`): o valor nunca é escrito no YAML, o `env` só recebe o que vem do contexto `secrets`.

**vars** são variáveis de configuração não secretas, definidas nas configurações do GitHub, no nível do repositório, da organização ou do ambiente. Como ficam fora do YAML, podem ser alteradas sem precisar de um commit, e servem para configurações usadas por vários workflows ou compartilhadas em todo o repositório, como o `APP_NAME`.

**secrets** são informações confidenciais, como chaves de API, tokens de acesso e senhas, como a `API_KEY`. Ficam mascarados nos logs, não podem ser lidos depois de salvos e nunca entram no YAML nem no código.

### GITHUB_TOKEN
É um token de autenticação gerado automaticamente pelo GitHub para cada execução de workflow no GitHub Actions, funcionando como uma credencial temporária que permite que as automações interajam com o repositório. Limitar as permissões é indicado para que o token tenha acesso apenas ao estritamente necessário: se uma action de terceiros ou um script for comprometido, o estrago fica limitado ao que o token pode fazer. No meu projeto, o CI e o deploy têm apenas `contents: read`, porque só precisam ler o código para executar. O `run-monitor` tem `permissions: {}`, porque não precisa de nenhuma permissão sobre o repositório.

### Ambientes
Um ambiente protegido cria barreiras de controle antes de entregar código em destinos críticos, como produção. Uma das funções é a aprovação manual, exigindo que uma ou mais pessoas aprovem o job de deploy antes de ele rodar. No meu projeto, o environment `production` tem revisor obrigatório, e a `PROD_DOMAIN`, definida nele, só fica visível para o job que declara esse environment.
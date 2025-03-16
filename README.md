# Insurance API
API RESTful simples para gerenciamento de seguros, com funcionalidades CRUD para criar, ler, atualizar e deletar seguros, além de um cálculo simples de cotação baseado na idade do cliente e no valor do seguro.

# 🚀 Motivação
A aplicação foi desenvolvida com o objetivo de fornecer uma interface para gestão de seguros, utilizando os melhores padrões de desenvolvimento, boas práticas de codificação e arquiteturas modernas, como a Arquitetura Hexagonal (Ports and Adapters). A ideia é manter a aplicação simples, fácil de testar e escalar.

# Principais Funcionalidades:
Create: Criação de um novo seguro.
Read: Leitura de informações de seguros existentes.
Update: Atualização de dados de um seguro.
Delete: Exclusão de um seguro.
Cálculo: Cálculo da cotação de um seguro baseado na idade do cliente e no valor do seguro.

# 🛠 Tecnologias Utilizadas
Java 11: A aplicação foi desenvolvida em Java 11 devido à sua robustez, desempenho e ampla aceitação no mercado.
Spring Boot: Framework para criação de APIs RESTful de maneira simples e rápida, com boas práticas e integração fácil com bancos de dados.
Maven: Gerenciador de dependências para construção do projeto.
JUnit e Mockito: Para testes unitários e de integração.
Docker: Para criação de contêineres e execução em qualquer ambiente com Docker instalado.

# 🧱 Arquitetura

## Arquitetura Hexagonal (Ports and Adapters)
Optamos por utilizar a Arquitetura Hexagonal, também conhecida como Ports and Adapters, para garantir que a aplicação fosse desenvolvida de maneira desacoplada e com facilidade de manutenção. A ideia central é separar a lógica de negócios (o "Core") dos mecanismos de comunicação externa, como bancos de dados, interfaces de usuário e APIs externas.

## Por que Hexagonal?
- Flexibilidade: A Arquitetura Hexagonal permite que diferentes implementações de mecanismos externos possam ser trocadas sem afetar a lógica de negócios.
- Testabilidade: O design desacoplado facilita a criação de testes unitários e de integração. Podemos testar a lógica central sem nos preocupar com detalhes de implementação externa.
- Escalabilidade: Essa arquitetura facilita a evolução do sistema sem grandes mudanças no código existente.

## Componentes Principais:
- Core (Lógica de Negócios): A lógica de negócios está no centro da aplicação, dentro da camada de Service.
- Ports (Interfaces): Interfaces que definem as operações que a aplicação precisa de fontes externas, como o InsuranceRepository.
- Adapters (Implementações): Adaptadores que implementam as interfaces de acesso a dados ou outras fontes externas. No nosso caso, o InsuranceRepositoryImpl é um exemplo de adaptador que interage com o banco de dados.

# 🧑‍💻 Boas Práticas de Desenvolvimento

A aplicação segue algumas das principais boas práticas de desenvolvimento de software:

## SOLID Principles
- Single Responsibility Principle (SRP): Cada classe tem uma única responsabilidade. Por exemplo, a InsuranceService cuida apenas da lógica de negócios e não do acesso ao banco de dados.
- Open/Closed Principle (OCP): As classes estão abertas para extensão, mas fechadas para modificação. Podemos adicionar novas funcionalidades sem modificar o código existente.
- Liskov Substitution Principle (LSP): Subtipos devem ser substituíveis por seus tipos base. A interface InsuranceRepository pode ser implementada de diferentes maneiras sem modificar o comportamento da aplicação.
- Interface Segregation Principle (ISP): As interfaces são pequenas e específicas. Por exemplo, temos diferentes adaptadores para interagir com diferentes fontes de dados.
- Dependency Inversion Principle (DIP): As classes de alto nível (lógica de negócios) dependem de abstrações, não de implementações concretas.

## Clean Code
O código foi escrito de forma legível e compreensível, com nomes de variáveis e métodos claros e com foco na simplicidade. Funções pequenas e específicas são preferidas para manter o código de fácil manutenção.

# 🧪 Testes
A aplicação foi desenvolvida seguindo a abordagem TDD (Test-Driven Development), onde os testes são escritos antes da implementação do código. Isso garante que a aplicação tenha uma boa cobertura de testes e que os requisitos sejam atendidos corretamente.

- Testes Unitários
Os testes unitários garantem que a lógica de negócios (service layer) funcione conforme o esperado. Usamos JUnit 5 e Mockito para simular dependências e testar os comportamentos de cada método isoladamente.

- Testes de Integração
Os testes de integração são usados para garantir que o sistema como um todo funcione corretamente. Eles validam o comportamento da aplicação em conjunto com o banco de dados.

# ⚙️ Como Executar a Aplicação

Passo 1: Clonar o Repositório
Clone o repositório para a sua máquina local:

Passo 2: Construir o Projeto
Se estiver usando Maven, rode o seguinte comando para construir o projeto:

Passo 3: Executar a Aplicação Localmente
Após a construção do projeto, você pode rodar a aplicação com o comando:

Passo 4: Acessar a API
Você pode acessar os endpoints da API através de ferramentas como Postman ou cURL.

Exemplo de requisição para criar um seguro (POST):
curl -X POST "http://localhost:8080/api/insurances" -H "Content-Type: application/json" -d '{
    "customerName": "John Doe",
    "value": 10000,
    "customerAge": 30
}'

# 🐳 Como Executar com Docker

Passo 1: Construir a Imagem Docker

Passo 2: Rodar a Aplicação em Docker
A API estará disponível no http://localhost:8080.

# 🎯 Conclusão
Esta aplicação foi desenvolvida utilizando boas práticas de desenvolvimento, arquitetura hexagonal e seguindo princípios como SOLID, Clean Code e TDD. A escolha da arquitetura hexagonal permite um código desacoplado, fácil de testar e de manter. A aplicação é simples, mas escalável, podendo ser estendida com novas funcionalidades de forma modular.
![thumbnail-Jetpack Compose](https://user-images.githubusercontent.com/8989346/190405552-1c63783b-2c38-4764-b758-12c2d5f52004.png)

## 🔨 Funcionalidades do projeto

O App Panucci permite visualizar os produtos em 3 principais categorias:

- Destaques do dia: melhores produtos ou mais pedidos
- Menu: todos os produtos
- Bebidas: todas as bebidas

Também, é possível acessar os detalhes do produto ao clicar em um dos produtos a partir da tela de destaques, menu ou bebidas. Por fim, é possível acessar a tela de pedido ao clicar no botão de pedir ou no FAB.

> O App não possui lógica de cálculo ou cadastro, é apenas uma implementação focada no visual e interação de navegação.

![App Panucci em execução](https://user-images.githubusercontent.com/8989346/205920654-2d7c88d7-4baa-462e-8aba-fc037b569f58.gif)

## ✔️ Técnicas e tecnologias utilizadas

Para implementar o App foram utilizadas as seguintes funcionalidades e tecnologias:

- Jetpack Compose
- Material Design 3
  - `Scaffold`
  - `NavigationBar` 
  - `Floating Action Button`
  - `Center Aligned Top App Bar`
  - `Card`
- Lazy Layout
- Slot API
- Navigation para Compose
  - `NavController`
  - `NavHost`
  - Grafo de navegação
  - Integração com componentes visuais
- `LaunchedEffect`
- Sealed class

## 📁 Acesso ao projeto

Você pode [acessar o código fonte do projeto](https://github.com/alura-cursos/jetpack-compose-navigation/tree/aula-5) ou [baixá-lo](https://github.com/alura-cursos/jetpack-compose-navigation/archive/refs/heads/aula-5.zip).

## 🛠️ Abrir e rodar o projeto

Após baixar o projeto, você pode abrir com o Android Studio. Para isso, na tela de launcher clique em:

Open an Existing Project (ou alguma opção similar)
Procure o local onde o projeto está e o selecione (Caso o projeto seja baixado via zip, é necessário extraí-lo antes de procurá-lo)
Por fim clique em OK
O Android Studio deve executar algumas tasks do Gradle para configurar o projeto, aguarde até finalizar. Ao finalizar as tasks, você pode executar o App 🏆

<!-- ## 📚 Mais informações do curso

**Faça um CTA (_call to action_) para o curso do projeto**

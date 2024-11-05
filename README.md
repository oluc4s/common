# Sample
## _Projeto Base_

[![](https://jitpack.io/v/oluc4s/common.svg)](https://jitpack.io/#oluc4s/common)

Este projeto serve como base para novos projetos, oferecendo dependências commons e configuração inicial. Siga as instruções para configurá-lo em seu ambiente.

## Instalação
**Passo 1: Configuração do JitPack**

Adicione o JitPack ao seu projeto para resolver dependências. No arquivo settings.gradle.kts, inclua o repositório:
```sh
dependencyResolutionManagement {
    repositories {
        maven("https://jitpack.io")
    }
}
```

**Passo 2: Adicionando a Dependência**

No arquivo build.gradle.kts do módulo, adicione a dependência para ambientes de produção:
```sh
implementation("com.github.oluc4s:common:x.x")
```
Substitua x.x pela versão desejada, conforme mostrado no badge acima.

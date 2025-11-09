# 🕹️ Detector de Movimento — Wear OS

## 🎯 Objetivo
Este projeto é uma **prova de conceito (POC)** para Wear OS que utiliza os **sensores de movimento do relógio** (acelerômetro ou aceleração linear) para detectar atividade física.  
Ele exibe em tempo real a **intensidade do movimento** e vibra o relógio quando há **movimento leve ou forte**.  

---

## 🧩 Funcionalidades

- 📈 **Leitura do sensor de aceleração**  
  Detecta movimento real do dispositivo em tempo real.

- 🎛️ **Modo Simulação (Mock)**  
  Permite testar o comportamento no emulador, simulando movimentos aleatórios.

- 💬 **Status em tempo real**  
  Mostra na tela se o relógio está **parado**, em **movimento leve** ou em **movimento forte**.

- 💥 **Feedback por vibração**  
  O relógio vibra com intensidades diferentes conforme o tipo de movimento detectado.

- 🕹️ **Interface simples e adaptada ao Wear OS**  
  Feita com `androidx.wear.compose` para rodar de forma nativa e fluida em smartwatches.

---

## ⚙️ Como funciona

1. Ao iniciar o app, ele tenta usar o sensor **`TYPE_LINEAR_ACCELERATION`**, que ignora a gravidade.  
   - Se disponível, o valor parado fica **0.0 m/s²**.  
   - Caso contrário, é exibida uma mensagem de sensor indisponível.

2. O app calcula a **magnitude da aceleração** e classifica o movimento:
   - **< 2.0** → “PARADO”  
   - **≥ 2.0** → “MOVIMENTO”  
   - **≥ 15.0** → “MOVIMENTO FORTE”  

3. Quando há movimento, o relógio **vibra** levemente ou fortemente.  

4. O botão **“Modo Simulação / Sensor Real”** alterna entre:
   - 🧪 Simulação (dados aleatórios — útil no emulador)  
   - 📡 Sensor real (dados do relógio físico)

---

## 🧱 Estrutura do Projeto

```bash
br.edu.utfpr.motiondetectorapp/
├── presentation/
│   ├── MainActivity.kt        # Lógica principal, sensores e controle de mock
│   └── ui/
│       └── MotionScreen.kt    # Interface principal (Wear Compose)
└── ui/
    └── theme/
        └── WearMotionTheme.kt # Tema do aplicativo
```

---

## 🧰 Tecnologias Utilizadas

- **Kotlin**
- **Wear OS Compose** (`androidx.wear.compose`)
- **SensorManager API**
- **VibratorManager API**
- **Jetpack Compose Animation**

---

## 🧪 Testando no Emulador

1. Crie um **emulador Wear OS** no Android Studio.  
2. Instale e rode o app.  
3. Use o botão **“Modo Simulação”** para gerar dados falsos de movimento.  

> 💡 No modo simulado, o relógio exibirá valores variando automaticamente e alternará o status entre “PARADO”, “MOVIMENTO” e “MOVIMENTO FORTE”.

---

## 📱 Testando em um Relógio Real

1. Conecte o relógio ao Android Studio via ADB (`adb connect <IP>`).  
2. Instale o app no dispositivo.  
3. Desative o **Modo Simulação**.  
4. Mova o relógio — o valor mudará e o relógio vibrará conforme o movimento.

---

## 🧑‍💻 Autoria

**Autor:** Jorge Gabriel Rodrigues
**Disciplina:** Desenvolvimento de Aplicativos para Objetos Portáveis  

---





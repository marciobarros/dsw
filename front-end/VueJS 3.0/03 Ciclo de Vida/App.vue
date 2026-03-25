<template>
  <p>Valor: {{ valor }}</p>
  <button @click="incrementa">Aumenta</button>
</template>

<script setup>
import { ref, onUpdated, onMounted, onUnmounted } from 'vue'

const valor = ref(0)

function incrementa() {
  valor.value = valor.value + 1
}

onMounted(() => { 
  console.log("Vai começar a brincadeira")
})

onUpdated(() => { 
  console.log("Atualizou o componente")
})

onUnmounted(() => { 
  console.log("Fim de jogo")
})
</script>
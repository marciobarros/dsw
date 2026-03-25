<script setup>
import {ref} from 'vue'

let count = ref(0)

function increment() {
  count.value = count.value + 1
}
</script>

<template>
  <h1>Hello Vue 3</h1>
  <button @click="increment">Count is: {{ count }}</button>
</template>

<script setup>
import {ref} from 'vue'
import TaskList from './TaskList.vue';
import TaskForm from './TaskForm.vue';

const tasks = ref([
      { title: "Aprender Java", done: true },
      { title: "Aprender back-end Web com Java", done: true },
      { title: "Aprender Vue.js básico", done: false },
      { title: "Aprender Vue.js avançado", done: false },
      { title: "Aprender roteamento com Vue.js", done: false }
    ])
</script>

<template>
  <h2 class='lista'>Minhas Tarefas:</h2>
  <task-list :tasks='tasks'></task-list>

  <h2 class='form'>Nova Tarefa:</h2>
  <task-form :tasks='tasks'></task-form>
</template>
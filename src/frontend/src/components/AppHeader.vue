<script setup>
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'

const props = defineProps({
  modelValue: {
    type: String,
    default: '',
  },
})

const emit = defineEmits(['update:modelValue', 'search'])

const router = useRouter()
const query = ref(props.modelValue)

watch(
  () => props.modelValue,
  (value) => {
    query.value = value
  },
)

function submitSearch() {
  emit('update:modelValue', query.value)
  emit('search', query.value)

  if (router.currentRoute.value.name === 'home') {
    return
  }

  router.push({ name: 'home', query: { search: query.value || undefined } })
}
</script>

<template>
  <header class="app-header">
    <router-link to="/" class="brand">
      <span class="brand-icon" aria-hidden="true">🛒</span>
      <span class="brand-name">Catalogia</span>
    </router-link>

    <form class="search-form" @submit.prevent="submitSearch">
      <input
        v-model="query"
        type="search"
        placeholder="Search products..."
        aria-label="Search products"
      />
    </form>
  </header>
</template>

<style scoped>
.app-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 1rem;
  padding: 1.25rem 1.5rem;
  border-bottom: 2px solid #111;
}

.brand {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  text-decoration: none;
  color: #111;
}

.brand-icon {
  font-size: 1.35rem;
}

.brand-name {
  font-family: Georgia, 'Times New Roman', serif;
  font-size: 1.75rem;
  font-weight: 700;
  letter-spacing: 0.02em;
}

.search-form {
  flex: 1;
  max-width: 520px;
}

.search-form input {
  width: 100%;
  box-sizing: border-box;
  padding: 0.65rem 1rem;
  border: 2px solid #111;
  border-radius: 999px;
  font: inherit;
  background: #fff;
}

.search-form input:focus {
  outline: 2px solid #111;
  outline-offset: 2px;
}
</style>

<script setup>
import { ref, watch } from 'vue'

const props = defineProps({
  categories: {
    type: Array,
    default: () => [],
  },
  minPrice: {
    type: [String, Number],
    default: '',
  },
  maxPrice: {
    type: [String, Number],
    default: '',
  },
  categoryId: {
    type: [String, Number],
    default: '',
  },
})

const emit = defineEmits(['update:filters'])

const priceOpen = ref(true)
const categoryOpen = ref(true)
const localMinPrice = ref(props.minPrice)
const localMaxPrice = ref(props.maxPrice)
const localCategoryId = ref(props.categoryId)

watch(
  () => [props.minPrice, props.maxPrice, props.categoryId],
  ([minPrice, maxPrice, categoryId]) => {
    localMinPrice.value = minPrice
    localMaxPrice.value = maxPrice
    localCategoryId.value = categoryId
  },
)

function applyFilters() {
  emit('update:filters', {
    minPrice: localMinPrice.value,
    maxPrice: localMaxPrice.value,
    categoryId: localCategoryId.value,
  })
}
</script>

<template>
  <aside class="filter-panel">
    <h2>Filter</h2>

    <section class="filter-section">
      <button type="button" class="section-toggle" @click="priceOpen = !priceOpen">
        Price
        <span>{{ priceOpen ? '▾' : '▸' }}</span>
      </button>
      <div v-show="priceOpen" class="section-body">
        <label>
          <span>$</span>
          <input
            v-model="localMinPrice"
            type="number"
            min="0"
            step="0.01"
            placeholder="Min Price"
            @change="applyFilters"
          />
        </label>
        <label>
          <span>$</span>
          <input
            v-model="localMaxPrice"
            type="number"
            min="0"
            step="0.01"
            placeholder="Maximum Price"
            @change="applyFilters"
          />
        </label>
      </div>
    </section>

    <section class="filter-section">
      <button type="button" class="section-toggle" @click="categoryOpen = !categoryOpen">
        Category
        <span>{{ categoryOpen ? '▾' : '▸' }}</span>
      </button>
      <div v-show="categoryOpen" class="section-body category-list">
        <label v-for="category in categories" :key="category.id" class="category-item">
          <input
            v-model="localCategoryId"
            type="radio"
            name="category"
            :value="category.id"
            @change="applyFilters"
          />
          <span>{{ category.name }}</span>
        </label>
        <label class="category-item">
          <input
            v-model="localCategoryId"
            type="radio"
            name="category"
            value=""
            @change="applyFilters"
          />
          <span>All categories</span>
        </label>
      </div>
    </section>
  </aside>
</template>

<style scoped>
.filter-panel {
  border: 2px solid #111;
  border-radius: 0.75rem;
  padding: 1rem;
  background: #fff;
}

.filter-panel h2 {
  margin: 0 0 1rem;
  font-size: 1.25rem;
  font-weight: 700;
  color: #111;
}

.filter-section + .filter-section {
  margin-top: 1rem;
}

.section-toggle {
  width: 100%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0.75rem;
  border: 2px solid #111;
  border-radius: 0.5rem;
  background: #fff;
  font: inherit;
  font-weight: 600;
  cursor: pointer;
}

.section-body {
  margin-top: 0.75rem;
  display: grid;
  gap: 0.75rem;
}

.section-body label {
  display: grid;
  grid-template-columns: auto 1fr;
  align-items: center;
  gap: 0.5rem;
}

.section-body input[type='number'] {
  width: 100%;
  box-sizing: border-box;
  padding: 0.55rem 0.75rem;
  border: 2px solid #111;
  border-radius: 0.5rem;
  font: inherit;
}

.category-list {
  gap: 0.5rem;
}

.category-item {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}
</style>

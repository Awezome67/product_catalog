<script setup>
import { onMounted, ref, watch } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '../components/AppHeader.vue'
import ProductFilter from '../components/ProductFilter.vue'
import ProductCard from '../components/ProductCard.vue'
import FloatingAddButton from '../components/FloatingAddButton.vue'
import { fetchProducts } from '../api/products'
import { fetchCategories } from '../api/categories'

const route = useRoute()

const products = ref([])
const categories = ref([])
const loading = ref(true)
const error = ref('')
const search = ref(route.query.search?.toString() ?? '')
const filters = ref({
  minPrice: '',
  maxPrice: '',
  categoryId: '',
})

async function loadProducts() {
  loading.value = true
  error.value = ''

  try {
    const params = {
      search: search.value || undefined,
      minPrice: filters.value.minPrice || undefined,
      maxPrice: filters.value.maxPrice || undefined,
      categoryId: filters.value.categoryId || undefined,
    }
    const { data } = await fetchProducts(params)
    products.value = data
  } catch {
    error.value = 'Could not load products. Make sure the backend is running.'
  } finally {
    loading.value = false
  }
}

async function loadCategories() {
  const { data } = await fetchCategories()
  categories.value = data
}

function handleFilterUpdate(nextFilters) {
  filters.value = nextFilters
  loadProducts()
}

watch(
  () => route.query.search,
  (value) => {
    search.value = value?.toString() ?? ''
    loadProducts()
  },
)

onMounted(async () => {
  await Promise.all([loadCategories(), loadProducts()])
})
</script>

<template>
  <div class="page">
    <AppHeader v-model="search" @search="loadProducts" />

    <div class="page-body">
      <ProductFilter
        :categories="categories"
        :min-price="filters.minPrice"
        :max-price="filters.maxPrice"
        :category-id="filters.categoryId"
        @update:filters="handleFilterUpdate"
      />

      <section class="catalog-content">
        <p v-if="loading" class="status">Loading products...</p>
        <p v-else-if="error" class="status error">{{ error }}</p>
        <p v-else-if="products.length === 0" class="status">No products found.</p>

        <div v-else class="product-grid">
          <ProductCard v-for="product in products" :key="product.id" :product="product" />
        </div>
      </section>
    </div>

    <FloatingAddButton />
  </div>
</template>

<style scoped>
.page {
  min-height: 100vh;
  background: #fff;
}

.page-body {
  display: grid;
  grid-template-columns: 240px 1fr;
  gap: 1.5rem;
  padding: 1.5rem;
}

.catalog-content {
  min-height: 320px;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
  gap: 1rem;
}

.status {
  margin: 0;
  padding: 1rem;
  border: 2px dashed #111;
  border-radius: 0.75rem;
  text-align: center;
}

.status.error {
  border-style: solid;
  color: #b71c1c;
}

@media (max-width: 900px) {
  .page-body {
    grid-template-columns: 1fr;
  }
}
</style>

import type { Product } from '../types'

export const products: Product[] = [
  {
    id: 1, name: 'Pro Wireless Headphones', category: 'Electronics',
    price: 6999, oldPrice: 8999, rating: 4.7, reviews: 328,
    image: 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80',
    badge: 'Best Seller'
  },
  {
    id: 2, name: 'Smart Watch X2', category: 'Electronics',
    price: 4499, oldPrice: 5999, rating: 4.5, reviews: 214,
    image: 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=800&q=80',
    badge: '20% OFF'
  },
  {
    id: 3, name: 'Minimal Running Shoes', category: 'Fashion',
    price: 3299, oldPrice: 4299, rating: 4.6, reviews: 187,
    image: 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=800&q=80'
  },
  {
    id: 4, name: 'Everyday Backpack', category: 'Fashion',
    price: 1899, oldPrice: 2499, rating: 4.4, reviews: 142,
    image: 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=800&q=80',
    badge: 'New'
  },
  {
    id: 5, name: 'Mechanical Keyboard', category: 'Electronics',
    price: 5999, oldPrice: 6999, rating: 4.8, reviews: 96,
    image: 'https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=800&q=80'
  },
  {
    id: 6, name: 'Ceramic Coffee Set', category: 'Home',
    price: 1499, oldPrice: 1999, rating: 4.3, reviews: 73,
    image: 'https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?auto=format&fit=crop&w=800&q=80'
  },
  {
    id: 7, name: 'Premium Cotton Hoodie', category: 'Fashion',
    price: 2199, oldPrice: 2999, rating: 4.5, reviews: 159,
    image: 'https://images.unsplash.com/photo-1556821840-3a63f95609a7?auto=format&fit=crop&w=800&q=80'
  },
  {
    id: 8, name: 'Portable Bluetooth Speaker', category: 'Electronics',
    price: 2499, oldPrice: 3299, rating: 4.6, reviews: 201,
    image: 'https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?auto=format&fit=crop&w=800&q=80'
  }
]
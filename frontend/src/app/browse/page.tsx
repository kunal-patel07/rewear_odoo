"use client"
import { useState, useEffect } from "react"
import { Search, Heart, Star } from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useRouter, useSearchParams } from "next/navigation"

export default function BrowsePage() {
  const [searchQuery, setSearchQuery] = useState("")
  const [selectedCategory, setSelectedCategory] = useState("all")
  const [sortBy, setSortBy] = useState("newest")

  const router = useRouter()
  const searchParams = useSearchParams()

  useEffect(() => {
    const categoryParam = searchParams.get("category")
    if (categoryParam) {
      setSelectedCategory(categoryParam)
    }
  }, [searchParams])

  const handleItemClick = (itemId: number) => {
    router.push(`/item/${itemId}`)
  }

  const items = [
    {
      id: 1,
      title: "Vintage Denim Jacket",
      price: 45,
      originalPrice: 120,
      image: "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=300&h=400&fit=crop",
      condition: "Like New",
      brand: "Levi's",
      seller: "Sarah J.",
      rating: 4.8,
      likes: 12,
      category: "men's",
    },
    {
      id: 2,
      title: "Designer Handbag",
      price: 85,
      originalPrice: 250,
      image: "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=300&h=400&fit=crop",
      condition: "Excellent",
      brand: "Coach",
      seller: "Emma_Fashion",
      rating: 4.9,
      likes: 24,
      category: "accessories",
    },
    {
      id: 3,
      title: "Running Sneakers",
      price: 35,
      originalPrice: 90,
      image: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&h=400&fit=crop",
      condition: "Good",
      brand: "Nike",
      seller: "Mike_Sports",
      rating: 4.7,
      likes: 8,
      category: "men's",
    },
    {
      id: 4,
      title: "Wool Sweater",
      price: 28,
      originalPrice: 75,
      image: "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=300&h=400&fit=crop",
      condition: "Like New",
      brand: "J.Crew",
      seller: "Style_Queen",
      rating: 4.6,
      likes: 15,
      category: "women's",
    },
    {
      id: 5,
      title: "Kids Summer Dress",
      price: 22,
      originalPrice: 45,
      image: "https://images.unsplash.com/photo-1519238263530-99bdd11df2ea?w=300&h=400&fit=crop",
      condition: "Excellent",
      brand: "Gap Kids",
      seller: "Mom_Closet",
      rating: 4.8,
      likes: 6,
      category: "kids",
    },
    {
      id: 6,
      title: "Leather Boots",
      price: 65,
      originalPrice: 150,
      image: "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=300&h=400&fit=crop",
      condition: "Good",
      brand: "Dr. Martens",
      seller: "Boot_Lover",
      rating: 4.5,
      likes: 18,
      category: "men's",
    },
    {
      id: 7,
      title: "Men's Casual Shirt",
      price: 25,
      originalPrice: 60,
      image: "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=300&h=400&fit=crop",
      condition: "Like New",
      brand: "Ralph Lauren",
      seller: "Classic_Style",
      rating: 4.7,
      likes: 11,
      category: "men's",
    },
    {
      id: 8,
      title: "Women's Blazer",
      price: 55,
      originalPrice: 140,
      image: "https://images.unsplash.com/photo-1594633312681-425c7b97ccd1?w=300&h=400&fit=crop",
      condition: "Excellent",
      brand: "Zara",
      seller: "Professional_Wear",
      rating: 4.9,
      likes: 20,
      category: "women's",
    },
  ]

  const filteredItems = items.filter((item) => {
    const matchesSearch =
      item.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
      item.brand.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesCategory = selectedCategory === "all" || item.category === selectedCategory
    return matchesSearch && matchesCategory
  })

  const sortedItems = [...filteredItems].sort((a, b) => {
    switch (sortBy) {
      case "price-low":
        return a.price - b.price
      case "price-high":
        return b.price - a.price
      case "popular":
        return b.likes - a.likes
      default:
        return b.id - a.id // newest first
    }
  })

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <Link href="/" className="flex-shrink-0 flex items-center">
                <Image src="/images/rewear-logo.svg" alt="Rewear Logo" width={120} height={40} className="h-8 w-auto" />
              </Link>
            </div>

            <nav className="hidden md:block">
              <div className="ml-10 flex items-baseline space-x-8">
                <Link
                  href="/"
                  className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Home
                </Link>
                <Link
                  href="/browse"
                  className="text-gray-900 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Browse
                </Link>
                <Link
                  href="/dashboard"
                  className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Dashboard
                </Link>
              </div>
            </nav>

            <div className="flex items-center space-x-4">
              <Link
                href="/login"
                className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
              >
                Login
              </Link>
              <Link
                href="/signup"
                className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md text-sm font-medium transition-colors"
              >
                Sign Up
              </Link>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* Search and Filters */}
        <div className="bg-white rounded-xl shadow-sm p-6 mb-8">
          <div className="flex flex-col lg:flex-row gap-4">
            {/* Search Bar */}
            <div className="flex-1 relative">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-400 w-5 h-5" />
              <input
                type="text"
                placeholder="Search items, brands, or sellers..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="w-full pl-10 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
              />
            </div>

            {/* Category Filter */}
            <select
              value={selectedCategory}
              onChange={(e) => setSelectedCategory(e.target.value)}
              className="px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 cursor-pointer"
            >
              <option value="all">All Categories</option>
              <option value="women's">Women's</option>
              <option value="men's">Men's</option>
              <option value="kids">Kids</option>
              <option value="accessories">Accessories</option>
            </select>

            {/* Sort By */}
            <select
              value={sortBy}
              onChange={(e) => setSortBy(e.target.value)}
              className="px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 cursor-pointer"
            >
              <option value="newest">Newest First</option>
              <option value="price-low">Price: Low to High</option>
              <option value="price-high">Price: High to Low</option>
              <option value="popular">Most Popular</option>
            </select>
          </div>
        </div>

        {/* Results Header */}
        <div className="flex justify-between items-center mb-6">
          <h1 className="text-2xl font-bold text-gray-900">
            {selectedCategory === "all"
              ? "All Items"
              : `${selectedCategory.charAt(0).toUpperCase() + selectedCategory.slice(1)} Items`}
            ({sortedItems.length} found)
          </h1>
          <Link
            href="/list-item"
            className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg transition-colors font-semibold hover-lift"
          >
            List Your Item
          </Link>
        </div>

        {/* Items Grid */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
          {sortedItems.map((item) => (
            <div
              key={item.id}
              onClick={() => handleItemClick(item.id)}
              className="bg-white rounded-xl shadow-lg overflow-hidden product-card"
            >
              <div className="relative">
                <Image
                  src={item.image || "/placeholder.svg"}
                  alt={item.title}
                  width={300}
                  height={300}
                  className="w-full h-64 object-cover"
                />
                <button
                  onClick={(e) => {
                    e.stopPropagation()
                    // Handle favorite logic
                  }}
                  className="absolute top-4 right-4 bg-white rounded-full p-2 hover:bg-gray-100 transition-colors hover-lift"
                >
                  <Heart className="w-4 h-4 text-gray-600" />
                </button>
                <span className="absolute top-4 left-4 bg-green-600 text-white px-2 py-1 rounded text-xs font-semibold">
                  {item.condition}
                </span>
                <div className="absolute bottom-4 right-4 bg-black bg-opacity-50 text-white px-2 py-1 rounded text-xs">
                  ❤️ {item.likes}
                </div>
              </div>
              <div className="p-4">
                <p className="text-sm text-gray-500 mb-1">{item.brand}</p>
                <h3 className="font-semibold text-gray-900 mb-2 line-clamp-2">{item.title}</h3>
                <div className="flex items-center justify-between mb-2">
                  <div className="flex items-center space-x-2">
                    <span className="text-lg font-bold text-green-600">${item.price}</span>
                    <span className="text-sm text-gray-500 line-through">${item.originalPrice}</span>
                  </div>
                </div>
                <div className="flex items-center justify-between text-sm text-gray-500">
                  <span>by {item.seller}</span>
                  <div className="flex items-center">
                    <Star className="w-4 h-4 text-yellow-400 fill-current mr-1" />
                    <span>{item.rating}</span>
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>

        {sortedItems.length === 0 && (
          <div className="text-center py-12">
            <div className="text-gray-400 mb-4">
              <Search className="w-16 h-16 mx-auto" />
            </div>
            <h3 className="text-lg font-semibold text-gray-900 mb-2">No items found</h3>
            <p className="text-gray-600 mb-4">
              {selectedCategory !== "all"
                ? `No ${selectedCategory} items match your search criteria`
                : "Try adjusting your search or filters"}
            </p>
            <Link
              href="/list-item"
              className="inline-flex items-center bg-green-600 hover:bg-green-700 text-white px-6 py-3 rounded-lg transition-colors font-semibold hover-lift"
            >
              {selectedCategory !== "all"
                ? `List the first ${selectedCategory} item`
                : "List the first item in this category"}
            </Link>
          </div>
        )}
      </div>
    </div>
  )
}

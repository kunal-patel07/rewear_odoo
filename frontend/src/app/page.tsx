"use client"
import { useState, useEffect } from "react"
import { Star, Users, Recycle, Heart, LogOut, User } from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { isAuthenticated, getUserInfoFromToken, logout } from "@/lib/auth"

export default function RewearLanding() {
  const [currentSlide, setCurrentSlide] = useState(0)
  const [mounted, setMounted] = useState(false)
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [userInfo, setUserInfo] = useState<{
    name?: string
    email?: string
    username?: string
  }>({})

  const router = useRouter()

  // Check authentication status and decode token
  useEffect(() => {
    setMounted(true)
    
    const checkAuth = () => {
      const authenticated = isAuthenticated()
      setIsLoggedIn(authenticated)
      
      if (authenticated) {
        const tokenUserInfo = getUserInfoFromToken()
        if (tokenUserInfo) {
          setUserInfo({
            email: tokenUserInfo.email,
            name: tokenUserInfo.name,
            username: tokenUserInfo.username,
          })
        } else {
          // If we can't get user info, token might be invalid
          setIsLoggedIn(false)
          logout(false) // Don't redirect, just clear token
        }
      }
    }

    checkAuth()
  }, [])

  useEffect(() => {
    if (!mounted) return
    
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % carouselImages.length)
    }, 8000)
    return () => clearInterval(timer)
  }, [mounted])

  const handleCategoryClick = (category: string) => {
    router.push(`/browse?category=${category.toLowerCase()}`)
  }

  const handleProductClick = (productId: number) => {
    router.push(`/item/${productId}`)
  }

  const handleLogout = () => {
    logout(false) // Don't redirect, just clear token
    setIsLoggedIn(false)
    setUserInfo({})
  }

  const carouselImages = [
    {
      src: "https://images.unsplash.com/photo-1441986300917-64674bd600d8?w=1920&h=1080&fit=crop",
      alt: "Fashion clothing store interior",
      title: "Premium Winter Collection",
      subtitle: "Discover quality pre-loved winter wear",
    },
    {
      src: "https://images.unsplash.com/photo-1445205170230-053b83016050?w=1920&h=1080&fit=crop",
      alt: "Hanging clothes in wardrobe", 
      title: "Summer Essentials",
      subtitle: "Bright and beautiful summer styles",
    },
    {
      src: "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?w=1920&h=1080&fit=crop",
      alt: "Vintage clothing collection",
      title: "Everyday Comfort", 
      subtitle: "Casual wear for every occasion",
    },
    {
      src: "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=1920&h=1080&fit=crop",
      alt: "Fashion accessories and shoes",
      title: "Sustainable Style",
      subtitle: "Fashion that cares for the planet",
    },
  ]

  const categories = [
    {
      name: "Men's",
      image: "https://images.unsplash.com/photo-1516257984-b1b4d707412e?w=400&h=300&fit=crop",
      count: "2,450+ items",
    },
    {
      name: "Women's",
      image: "https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=400&h=300&fit=crop",
      count: "3,200+ items",
    },
    {
      name: "Kids",
      image: "https://images.unsplash.com/photo-1519238263530-99bdd11df2ea?w=400&h=300&fit=crop",
      count: "1,800+ items",
    },
  ]

  const featuredProducts = [
    {
      id: 1,
      name: "Vintage Denim Jacket",
      price: "$45",
      originalPrice: "$120",
      image: "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=300&h=400&fit=crop",
      condition: "Like New",
      brand: "Levi's",
    },
    {
      id: 2,
      name: "Designer Handbag",
      price: "$85",
      originalPrice: "$250",
      image: "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=300&h=400&fit=crop",
      condition: "Excellent",
      brand: "Coach",
    },
    {
      id: 3,
      name: "Running Sneakers",
      price: "$35",
      originalPrice: "$90",
      image: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&h=400&fit=crop",
      condition: "Good",
      brand: "Nike",
    },
    {
      id: 4,
      name: "Wool Sweater",
      price: "$28",
      originalPrice: "$75",
      image: "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=300&h=400&fit=crop",
      condition: "Like New",
      brand: "J.Crew",
    },
  ]

  const testimonials = [
    {
      name: "Sarah Johnson",
      text: "I've saved over $500 while completely refreshing my wardrobe. Rewear makes sustainable fashion so easy!",
      rating: 5,
      avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=100&h=100&fit=crop&crop=face",
    },
    {
      name: "Mike Chen",
      text: "Great quality items and fast shipping. Love that I'm helping the environment while looking good.",
      rating: 5,
      avatar: "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=100&h=100&fit=crop&crop=face",
    },
    {
      name: "Emma Davis",
      text: "The kids section is amazing! My daughter loves her 'new' clothes and I love the prices.",
      rating: 5,
      avatar: "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=100&h=100&fit=crop&crop=face",
    },
  ]

  return (
    <div className="min-h-screen bg-white">
      {/* Header */}
      <header className="bg-white shadow-sm border-b relative z-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <div className="flex-shrink-0 flex items-center">
                <Image src="/images/rewear-logo.svg" alt="Rewear Logo" width={120} height={40} className="h-8 w-auto" />
              </div>
            </div>

            <nav className="hidden md:block">
              <div className="ml-10 flex items-baseline space-x-8">
                <Link
                  href="/"
                  className="text-gray-900 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Home
                </Link>
                <Link
                  href="/browse"
                  className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Browse
                </Link>
                {isLoggedIn && (
                  <Link
                    href="/dashboard"
                    className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                  >
                    Dashboard
                  </Link>
                )}
                <Link
                  href="#"
                  className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  How It Works
                </Link>
                <Link
                  href="#"
                  className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  About
                </Link>
              </div>
            </nav>

            <div className="flex items-center space-x-4">
              {isLoggedIn ? (
                // Authenticated user menu
                <>
                  <Link
                    href="/list-item"
                    className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                  >
                    List Item
                  </Link>
                  <div className="flex items-center space-x-3">
                    <div className="flex items-center space-x-2">
                      <User className="w-4 h-4 text-gray-500" />
                      <span className="text-sm text-gray-700">
                        {userInfo.name || userInfo.username || 'User'}
                      </span>
                    </div>
                    <button
                      onClick={handleLogout}
                      className="text-gray-500 hover:text-red-600 p-2 transition-colors"
                      title="Logout"
                    >
                      <LogOut className="w-4 h-4" />
                    </button>
                  </div>
                </>
              ) : (
                // Guest user menu
                <>
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
                </>
              )}
            </div>
          </div>
        </div>
      </header>

      {/* Hero Section with Enhanced Background Carousel */}
      <section className="relative py-20 overflow-hidden min-h-[80vh] flex items-center bg-gradient-to-br from-green-600 to-blue-600">
        {/* Background Carousel */}
        {mounted ? (
          <div className="absolute inset-0 z-0">
            <div className="relative w-full h-full">
              {carouselImages.map((image, index) => (
                <div
                  key={index}
                  className={`absolute inset-0 transition-all duration-[3000ms] ease-in-out ${
                    index === currentSlide ? "opacity-100 scale-100" : "opacity-0 scale-105"
                  }`}
                >
                  <Image
                    src={image.src}
                    alt={image.alt}
                    fill
                    className="object-cover"
                    priority={index === 0}
                    sizes="100vw"
                  />
                  <div className="absolute inset-0 bg-gradient-to-r from-black/80 via-black/60 to-black/80"></div>
                </div>
              ))}
            </div>
          </div>
        ) : (
          <div className="absolute inset-0 z-0 bg-gradient-to-br from-green-600 to-blue-600 opacity-80" />
        )}

        {/* Animated Background Elements */}
        <div className="absolute inset-0 z-10">
          <div className="absolute top-20 left-10 w-32 h-32 bg-green-400/20 rounded-full blur-xl animate-pulse"></div>
          <div className="absolute bottom-20 right-10 w-40 h-40 bg-blue-400/20 rounded-full blur-xl animate-pulse delay-1000"></div>
          <div className="absolute top-1/2 left-1/4 w-24 h-24 bg-purple-400/20 rounded-full blur-xl animate-pulse delay-2000"></div>
        </div>

        {/* Content */}
        <div className="relative z-20 max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 w-full">
          <div className="text-center">
            <div className="mb-4 animate-fade-in-up">
              <span className="text-green-400 text-lg font-semibold">
                {mounted ? carouselImages[currentSlide]?.subtitle : "Discover quality pre-loved fashion"}
              </span>
            </div>
            <h1 className="text-4xl md:text-6xl font-bold text-white mb-6 animate-fade-in-up delay-200">
              Give Your Clothes a <span className="text-green-400 animate-pulse">Second Life</span>
            </h1>
            <p className="text-xl text-gray-200 mb-8 max-w-3xl mx-auto animate-fade-in-up delay-400">
              Join thousands of fashion lovers who are swapping, selling, and buying pre-loved clothing. Sustainable
              fashion that's good for your wallet and the planet.
            </p>
            <div className="flex flex-col sm:flex-row gap-4 justify-center animate-fade-in-up delay-600">
              {isLoggedIn ? (
                // Authenticated user CTAs
                <>
                  <Link
                    href="/list-item"
                    className="bg-green-600 hover:bg-green-700 text-white px-8 py-4 rounded-lg text-lg font-semibold transition-all duration-300 transform hover:scale-105 hover:shadow-lg"
                  >
                    List Your Item
                  </Link>
                  <Link
                    href="/browse"
                    className="border-2 border-white text-white hover:bg-white hover:text-green-600 px-8 py-4 rounded-lg text-lg font-semibold transition-all duration-300 transform hover:scale-105"
                  >
                    Browse Items
                  </Link>
                </>
              ) : (
                // Guest user CTAs
                <>
                  <Link
                    href="/signup"
                    className="bg-green-600 hover:bg-green-700 text-white px-8 py-4 rounded-lg text-lg font-semibold transition-all duration-300 transform hover:scale-105 hover:shadow-lg"
                  >
                    Start Swapping
                  </Link>
                  <Link
                    href="/browse"
                    className="border-2 border-white text-white hover:bg-white hover:text-green-600 px-8 py-4 rounded-lg text-lg font-semibold transition-all duration-300 transform hover:scale-105"
                  >
                    Browse Items
                  </Link>
                </>
              )}
            </div>
          </div>
        </div>

        {/* Carousel Indicators */}
        {mounted && (
          <div className="absolute bottom-8 left-1/2 transform -translate-x-1/2 z-30 carousel-indicators flex space-x-3">
            {carouselImages.map((_, index) => (
              <button
                key={index}
                onClick={() => setCurrentSlide(index)}
                className={`w-3 h-3 rounded-full transition-all duration-500 ${
                  index === currentSlide 
                    ? "bg-white shadow-lg scale-125" 
                    : "bg-white/40 hover:bg-white/60 hover:scale-110"
                }`}
                aria-label={`Go to slide ${index + 1}`}
              />
            ))}
          </div>
        )}

        {/* Floating Elements Animation */}
        <div className="absolute inset-0 z-10 pointer-events-none">
          <div className="absolute top-1/4 left-1/3 w-2 h-2 bg-white/40 rounded-full animate-float"></div>
          <div className="absolute top-3/4 left-1/4 w-3 h-3 bg-green-400/60 rounded-full animate-float delay-1000"></div>
          <div className="absolute top-1/2 right-1/3 w-2 h-2 bg-blue-400/40 rounded-full animate-float delay-2000"></div>
          <div className="absolute bottom-1/4 right-1/4 w-4 h-4 bg-purple-400/30 rounded-full animate-float delay-3000"></div>
        </div>
      </section>

      {/* Categories Section */}
      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-center text-gray-900 mb-12">Shop by Category</h2>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {categories.map((category, index) => (
              <div
                key={index}
                onClick={() => handleCategoryClick(category.name)}
                className="bg-white rounded-xl shadow-lg overflow-hidden category-hover"
              >
                <div className="relative h-80 overflow-hidden">
                  <Image
                    src={category.image || "/placeholder.svg"}
                    alt={category.name}
                    fill
                    className="object-cover transition-transform duration-500 hover:scale-110"
                  />
                  <div className="category-overlay absolute inset-0 bg-gradient-to-t from-black/60 via-transparent to-transparent transition-all duration-300"></div>
                  <div className="absolute bottom-0 left-0 right-0 p-6 text-white">
                    <h3 className="text-2xl font-bold mb-1">{category.name}</h3>
                    <p className="text-white/80 text-sm">{category.count}</p>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Product Listings */}
      <section className="py-16 bg-white">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center mb-12">
            <h2 className="text-3xl font-bold text-gray-900">Trending Items</h2>
            <button className="text-green-600 hover:text-green-700 font-semibold transition-colors">View All →</button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {featuredProducts.map((product) => (
              <div
                key={product.id}
                onClick={() => handleProductClick(product.id)}
                className="bg-white rounded-xl shadow-lg overflow-hidden product-card"
              >
                <div className="relative overflow-hidden">
                  <Image
                    src={product.image || "/placeholder.svg"}
                    alt={product.name}
                    width={300}
                    height={400}
                    className="w-full h-64 object-cover transition-transform duration-300 hover:scale-110"
                  />
                  <button
                    onClick={(e) => {
                      e.stopPropagation()
                      // Handle favorite logic
                    }}
                    className="absolute top-4 right-4 bg-white rounded-full p-2 hover:bg-gray-100 transition-colors hover-lift"
                  >
                    <Heart className="w-5 h-5 text-gray-600" />
                  </button>
                  <span className="absolute top-4 left-4 bg-green-600 text-white px-2 py-1 rounded text-sm font-semibold">
                    {product.condition}
                  </span>
                </div>
                <div className="p-4">
                  <p className="text-sm text-gray-500 mb-1">{product.brand}</p>
                  <h3 className="font-semibold text-gray-900 mb-2">{product.name}</h3>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center space-x-2">
                      <span className="text-lg font-bold text-green-600">{product.price}</span>
                      <span className="text-sm text-gray-500 line-through">{product.originalPrice}</span>
                    </div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Impact Metrics */}
      <section className="py-16 bg-green-600">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="text-center mb-12">
            <h2 className="text-3xl font-bold text-white mb-4">Our Impact Together</h2>
            <p className="text-green-100 text-lg">Making a difference, one piece of clothing at a time</p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div className="text-center">
              <div className="bg-white bg-opacity-20 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                <Users className="w-8 h-8 text-white" />
              </div>
              <div className="text-3xl font-bold text-white mb-2">50,000+</div>
              <div className="text-green-100">Happy Members</div>
            </div>

            <div className="text-center">
              <div className="bg-white bg-opacity-20 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                <Recycle className="w-8 h-8 text-white" />
              </div>
              <div className="text-3xl font-bold text-white mb-2">2M+</div>
              <div className="text-green-100">Items Rehomed</div>
            </div>

            <div className="text-center">
              <div className="bg-white bg-opacity-20 rounded-full w-16 h-16 flex items-center justify-center mx-auto mb-4">
                <Heart className="w-8 h-8 text-white" />
              </div>
              <div className="text-3xl font-bold text-white mb-2">500 tons</div>
              <div className="text-green-100">CO₂ Saved</div>
            </div>
          </div>
        </div>
      </section>

      {/* Testimonials */}
      <section className="py-16 bg-gray-50">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <h2 className="text-3xl font-bold text-center text-gray-900 mb-12">What Our Community Says</h2>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {testimonials.map((testimonial, index) => (
              <div
                key={index}
                className="bg-white rounded-xl shadow-lg p-6 transform hover:scale-105 transition-transform duration-300"
              >
                <div className="flex items-center mb-4">
                  {[...Array(testimonial.rating)].map((_, i) => (
                    <Star key={i} className="w-5 h-5 text-yellow-400 fill-current" />
                  ))}
                </div>
                <p className="text-gray-600 mb-6">"{testimonial.text}"</p>
                <div className="flex items-center">
                  <Image
                    src={testimonial.avatar || "/placeholder.svg"}
                    alt={testimonial.name}
                    width={48}
                    height={48}
                    className="w-12 h-12 rounded-full mr-4"
                  />
                  <div>
                    <div className="font-semibold text-gray-900">{testimonial.name}</div>
                    <div className="text-sm text-gray-500">Verified Buyer</div>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="bg-gray-900 text-white py-12">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="grid grid-cols-1 md:grid-cols-4 gap-8">
            <div>
              <div className="flex items-center mb-4">
                <Image
                  src="/images/rewear-logo.svg"
                  alt="Rewear Logo"
                  width={120}
                  height={40}
                  className="h-8 w-auto brightness-0 invert"
                />
              </div>
              <p className="text-gray-300 mb-4">
                Sustainable fashion marketplace connecting conscious consumers with quality pre-loved clothing.
              </p>
            </div>

            <div>
              <h4 className="font-semibold mb-4">Shop</h4>
              <ul className="space-y-2 text-gray-300">
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Women's
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Men's
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Kids
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Accessories
                  </a>
                </li>
              </ul>
            </div>

            <div>
              <h4 className="font-semibold mb-4">Support</h4>
              <ul className="space-y-2 text-gray-300">
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Help Center
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Shipping Info
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Returns
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Contact Us
                  </a>
                </li>
              </ul>
            </div>

            <div>
              <h4 className="font-semibold mb-4">Company</h4>
              <ul className="space-y-2 text-gray-300">
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    About Us
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Careers
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Press
                  </a>
                </li>
                <li>
                  <a href="#" className="hover:text-green-400 transition-colors">
                    Sustainability
                  </a>
                </li>
              </ul>
            </div>
          </div>

          <div className="border-t border-gray-800 mt-8 pt-8 text-center text-gray-400">
            <p>&copy; 2024 Rewear. All rights reserved.</p>
          </div>
        </div>
      </footer>
    </div>
  )
}

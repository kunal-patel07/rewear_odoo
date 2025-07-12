"use client"

import { useState, useEffect } from "react"
import { Settings, Edit, Plus, Heart, Star, Eye, LogOut } from "lucide-react"
import Image from "next/image"
import Link from "next/link"
import { useRouter } from "next/navigation"
import { isAuthenticated, getUserInfoFromToken, logout } from "@/lib/auth"

export default function UserDashboard() {
  const [activeTab, setActiveTab] = useState("overview")
  const [userInfo, setUserInfo] = useState({
    name: "User",
    username: "@user",
    email: "user@example.com",
    joinDate: "January 2024",
    location: "Location",
    bio: "Fashion enthusiast who loves sustainable style. Always looking for unique pieces to add to my wardrobe!",
    avatar: "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=200&h=200&fit=crop&crop=face",
    stats: {
      listings: 12,
      purchases: 8,
      favorites: 24,
      rating: 4.8,
    },
  })
  const [loading, setLoading] = useState(true)
  const router = useRouter()

  useEffect(() => {
    // Check if user is authenticated
    if (!isAuthenticated()) {
      router.push('/login')
      return
    }

    // Get user info from token
    const tokenUserInfo = getUserInfoFromToken()
    if (tokenUserInfo) {
      setUserInfo(prev => ({
        ...prev,
        email: tokenUserInfo.email || prev.email,
        name: tokenUserInfo.name || prev.name,
        username: tokenUserInfo.username || prev.username,
      }))
    } else {
      // Token is invalid, redirect to login
      router.push('/login')
      return
    }
    
    setLoading(false)
  }, [router])

  const handleLogout = () => {
    logout(true) // Redirect to login after logout
  }

  const handleItemClick = (itemId: number) => {
    router.push(`/item/${itemId}`)
  }

  const myListings = [
    {
      id: 1,
      title: "Vintage Denim Jacket",
      price: "$45",
      image: "https://images.unsplash.com/photo-1551698618-1dfe5d97d256?w=300&h=400&fit=crop",
      status: "Active",
      views: 23,
      likes: 5,
    },
    {
      id: 2,
      title: "Designer Handbag",
      price: "$85",
      image: "https://images.unsplash.com/photo-1584917865442-de89df76afd3?w=300&h=400&fit=crop",
      status: "Sold",
      views: 45,
      likes: 12,
    },
    {
      id: 3,
      title: "Wool Sweater",
      price: "$28",
      image: "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=300&h=400&fit=crop",
      status: "Active",
      views: 18,
      likes: 3,
    },
    {
      id: 4,
      title: "Summer Dress",
      price: "$35",
      image: "https://images.unsplash.com/photo-1515372039744-b8f02a3ae446?w=300&h=400&fit=crop",
      status: "Active",
      views: 31,
      likes: 8,
    },
  ]

  const myPurchases = [
    {
      id: 1,
      title: "Running Sneakers",
      price: "$35",
      image: "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=300&h=400&fit=crop",
      seller: "Prince",
      purchaseDate: "2024-01-15",
      status: "Delivered",
    },
    {
      id: 2,
      title: "Leather Boots",
      price: "$65",
      image: "https://images.unsplash.com/photo-1549298916-b41d501d3772?w=300&h=400&fit=crop",
      seller: "Emma_Style",
      purchaseDate: "2024-01-10",
      status: "Delivered",
    },
    {
      id: 3,
      title: "Casual Shirt",
      price: "$22",
      image: "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=300&h=400&fit=crop",
      seller: "Mike_Fashion",
      purchaseDate: "2024-01-08",
      status: "In Transit",
    },
    {
      id: 4,
      title: "Winter Coat",
      price: "$95",
      image: "https://images.unsplash.com/photo-1539533018447-63fcce2678e3?w=300&h=400&fit=crop",
      seller: "StyleQueen",
      purchaseDate: "2024-01-05",
      status: "Delivered",
    },
  ]

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-50 flex items-center justify-center">
        <div className="animate-spin rounded-full h-32 w-32 border-b-2 border-green-600"></div>
      </div>
    )
  }

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
                  href="#"
                  className="text-gray-500 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Browse
                </Link>
                <Link
                  href="/dashboard"
                  className="text-gray-900 hover:text-green-600 px-3 py-2 text-sm font-medium transition-colors"
                >
                  Dashboard
                </Link>
              </div>
            </nav>

            <div className="flex items-center space-x-4">
              <button className="text-gray-500 hover:text-green-600 p-2">
                <Settings className="w-5 h-5" />
              </button>
              <button 
                onClick={handleLogout}
                className="text-gray-500 hover:text-red-600 p-2 transition-colors"
                title="Logout"
              >
                <LogOut className="w-5 h-5" />
              </button>
              <div className="w-8 h-8 rounded-full overflow-hidden">
                <Image
                  src={userInfo.avatar || "/placeholder.svg"}
                  alt="Profile"
                  width={32}
                  height={32}
                  className="w-full h-full object-cover"
                />
              </div>
            </div>
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {/* User Profile Section */}
        <div className="bg-white rounded-xl shadow-lg p-8 mb-8">
          <div className="flex flex-col lg:flex-row gap-8">
            {/* Profile Picture */}
            <div className="flex-shrink-0">
              <div className="relative">
                <Image
                  src={userInfo.avatar || "/placeholder.svg"}
                  alt="Profile"
                  width={150}
                  height={150}
                  className="w-32 h-32 lg:w-40 lg:h-40 rounded-full object-cover border-4 border-green-100"
                />
                <button className="absolute bottom-2 right-2 bg-green-600 text-white p-2 rounded-full hover:bg-green-700 transition-colors">
                  <Edit className="w-4 h-4" />
                </button>
              </div>
            </div>

            {/* User Info */}
            <div className="flex-1 grid grid-cols-1 lg:grid-cols-2 gap-6">
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Full Name</label>
                  <div className="bg-gray-50 px-4 py-3 rounded-lg border">
                    <span className="text-gray-900">{userInfo.name}</span>
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Username</label>
                  <div className="bg-gray-50 px-4 py-3 rounded-lg border">
                    <span className="text-gray-900">{userInfo.username}</span>
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Email</label>
                  <div className="bg-gray-50 px-4 py-3 rounded-lg border">
                    <span className="text-gray-900">{userInfo.email}</span>
                  </div>
                </div>
              </div>

              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Location</label>
                  <div className="bg-gray-50 px-4 py-3 rounded-lg border">
                    <span className="text-gray-900">{userInfo.location}</span>
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Member Since</label>
                  <div className="bg-gray-50 px-4 py-3 rounded-lg border">
                    <span className="text-gray-900">{userInfo.joinDate}</span>
                  </div>
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-1">Rating</label>
                  <div className="bg-gray-50 px-4 py-3 rounded-lg border flex items-center">
                    <Star className="w-5 h-5 text-yellow-400 fill-current mr-2" />
                    <span className="text-gray-900">{userInfo.stats.rating}/5.0</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Bio Section */}
          <div className="mt-6">
            <label className="block text-sm font-medium text-gray-700 mb-2">About</label>
            <div className="bg-gray-50 px-4 py-4 rounded-lg border">
              <p className="text-gray-900">{userInfo.bio}</p>
            </div>
          </div>

          {/* Stats */}
          <div className="mt-8 grid grid-cols-2 lg:grid-cols-4 gap-4">
            <div className="bg-green-50 p-4 rounded-lg text-center">
              <div className="text-2xl font-bold text-green-600">{userInfo.stats.listings}</div>
              <div className="text-sm text-gray-600">Active Listings</div>
            </div>
            <div className="bg-blue-50 p-4 rounded-lg text-center">
              <div className="text-2xl font-bold text-blue-600">{userInfo.stats.purchases}</div>
              <div className="text-sm text-gray-600">Purchases</div>
            </div>
            <div className="bg-purple-50 p-4 rounded-lg text-center">
              <div className="text-2xl font-bold text-purple-600">{userInfo.stats.favorites}</div>
              <div className="text-sm text-gray-600">Favorites</div>
            </div>
            <div className="bg-yellow-50 p-4 rounded-lg text-center">
              <div className="text-2xl font-bold text-yellow-600">{userInfo.stats.rating}</div>
              <div className="text-sm text-gray-600">Rating</div>
            </div>
          </div>
        </div>

        {/* My Listings Section */}
        <div className="bg-white rounded-xl shadow-lg p-6 mb-8">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-900">My Listings</h2>
            <button className="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-lg flex items-center transition-colors">
              <Plus className="w-4 h-4 mr-2" />
              Add New Item
            </button>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {myListings.map((item) => (
              <div
                key={item.id}
                onClick={() => handleItemClick(item.id)}
                className="bg-gray-50 rounded-lg overflow-hidden product-card"
              >
                <div className="relative">
                  <Image
                    src={item.image || "/placeholder.svg"}
                    alt={item.title}
                    width={300}
                    height={200}
                    className="w-full h-48 object-cover"
                  />
                  <span
                    className={`absolute top-2 left-2 px-2 py-1 rounded text-xs font-semibold ${
                      item.status === "Active" ? "bg-green-100 text-green-800" : "bg-gray-100 text-gray-800"
                    }`}
                  >
                    {item.status}
                  </span>
                </div>
                <div className="p-4">
                  <h3 className="font-semibold text-gray-900 mb-2">{item.title}</h3>
                  <p className="text-lg font-bold text-green-600 mb-2">{item.price}</p>
                  <div className="flex justify-between text-sm text-gray-500">
                    <span className="flex items-center">
                      <Eye className="w-4 h-4 mr-1" />
                      {item.views}
                    </span>
                    <span className="flex items-center">
                      <Heart className="w-4 h-4 mr-1" />
                      {item.likes}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>

        {/* My Purchases Section */}
        <div className="bg-white rounded-xl shadow-lg p-6">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-900">My Purchases</h2>
            <Link href="#" className="text-green-600 hover:text-green-700 font-semibold">
              View All →
            </Link>
          </div>

          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-6">
            {myPurchases.map((item) => (
              <div
                key={item.id}
                onClick={() => handleItemClick(item.id)}
                className="bg-gray-50 rounded-lg overflow-hidden product-card"
              >
                <div className="relative">
                  <Image
                    src={item.image || "/placeholder.svg"}
                    alt={item.title}
                    width={300}
                    height={200}
                    className="w-full h-48 object-cover"
                  />
                  <span
                    className={`absolute top-2 left-2 px-2 py-1 rounded text-xs font-semibold ${
                      item.status === "Delivered"
                        ? "bg-green-100 text-green-800"
                        : item.status === "In Transit"
                          ? "bg-blue-100 text-blue-800"
                          : "bg-yellow-100 text-yellow-800"
                    }`}
                  >
                    {item.status}
                  </span>
                </div>
                <div className="p-4">
                  <h3 className="font-semibold text-gray-900 mb-1">{item.title}</h3>
                  <p className="text-sm text-gray-600 mb-1">Seller: {item.seller}</p>
                  <p className="text-lg font-bold text-green-600 mb-2">{item.price}</p>
                  <p className="text-xs text-gray-500">Purchased: {item.purchaseDate}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  )
}

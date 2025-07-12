"use client"
import type React from "react"

import { useState } from "react"
import { ArrowLeft, X, Plus, Camera } from "lucide-react"
import Image from "next/image"
import Link from "next/link"

export default function ListItemPage() {
  const [formData, setFormData] = useState({
    title: "",
    description: "",
    category: "",
    brand: "",
    size: "",
    condition: "",
    price: "",
    originalPrice: "",
    color: "",
    material: "",
  })

  const [mainImage, setMainImage] = useState<string | null>(null)
  const [additionalImages, setAdditionalImages] = useState<string[]>([])

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    })
  }

  const handleMainImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0]
    if (file) {
      const reader = new FileReader()
      reader.onload = (e) => {
        setMainImage(e.target?.result as string)
      }
      reader.readAsDataURL(file)
    }
  }

  const handleAdditionalImageUpload = (e: React.ChangeEvent<HTMLInputElement>) => {
    const files = Array.from(e.target.files || [])
    files.forEach((file) => {
      const reader = new FileReader()
      reader.onload = (e) => {
        setAdditionalImages((prev) => [...prev, e.target?.result as string])
      }
      reader.readAsDataURL(file)
    })
  }

  const removeAdditionalImage = (index: number) => {
    setAdditionalImages((prev) => prev.filter((_, i) => i !== index))
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    console.log("Form submitted:", { formData, mainImage, additionalImages })
    // Handle form submission logic here
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

            <div className="flex items-center space-x-4">
              <Link href="/" className="inline-flex items-center text-gray-500 hover:text-green-600 transition-colors">
                <ArrowLeft className="w-4 h-4 mr-2" />
                Back to Home
              </Link>
            </div>
          </div>
        </div>
      </header>

      {/* Search Bar */}
      <div className="bg-white border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4">
          <div className="relative">
            <input
              type="text"
              placeholder="Search for similar items..."
              className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 pr-10"
            />
            <button className="absolute right-3 top-1/2 transform -translate-y-1/2 text-gray-400 hover:text-gray-600">
              <svg className="w-5 h-5" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path
                  strokeLinecap="round"
                  strokeLinejoin="round"
                  strokeWidth={2}
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                />
              </svg>
            </button>
          </div>
        </div>
      </div>

      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-gray-900 mb-2">List Your Item</h1>
          <p className="text-gray-600">Share your pre-loved fashion with the Rewear community</p>
        </div>

        <form onSubmit={handleSubmit} className="bg-white rounded-xl shadow-lg p-8">
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
            {/* Left Column - Main Product Image */}
            <div>
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Product Image</h3>
              <div className="relative">
                {mainImage ? (
                  <div className="relative w-full h-96 rounded-lg overflow-hidden border-2 border-gray-200">
                    <Image src={mainImage || "/placeholder.svg"} alt="Product" fill className="object-cover" />
                    <button
                      type="button"
                      onClick={() => setMainImage(null)}
                      className="absolute top-2 right-2 bg-red-500 text-white rounded-full p-1 hover:bg-red-600 transition-colors"
                    >
                      <X className="w-4 h-4" />
                    </button>
                  </div>
                ) : (
                  <label className="flex flex-col items-center justify-center w-full h-96 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 hover:bg-gray-100 transition-colors">
                    <div className="flex flex-col items-center justify-center pt-5 pb-6">
                      <Camera className="w-12 h-12 mb-4 text-gray-400" />
                      <p className="mb-2 text-sm text-gray-500">
                        <span className="font-semibold">Click to upload</span> main product image
                      </p>
                      <p className="text-xs text-gray-500">PNG, JPG or JPEG (MAX. 10MB)</p>
                    </div>
                    <input type="file" className="hidden" accept="image/*" onChange={handleMainImageUpload} />
                  </label>
                )}
              </div>
            </div>

            {/* Right Column - Product Details */}
            <div>
              <h3 className="text-lg font-semibold text-gray-900 mb-4">Product Details</h3>
              <div className="space-y-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Product Name *</label>
                  <input
                    type="text"
                    name="title"
                    required
                    value={formData.title}
                    onChange={handleInputChange}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                    placeholder="e.g., Vintage Denim Jacket"
                  />
                </div>

                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Description *</label>
                  <textarea
                    name="description"
                    required
                    rows={6}
                    value={formData.description}
                    onChange={handleInputChange}
                    className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500 resize-none"
                    placeholder="Describe your item's condition, style, fit, and any other details buyers should know..."
                  />
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Category *</label>
                    <select
                      name="category"
                      required
                      value={formData.category}
                      onChange={handleInputChange}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                    >
                      <option value="">Select Category</option>
                      <option value="womens">Women's</option>
                      <option value="mens">Men's</option>
                      <option value="kids">Kids</option>
                      <option value="accessories">Accessories</option>
                    </select>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Brand</label>
                    <input
                      type="text"
                      name="brand"
                      value={formData.brand}
                      onChange={handleInputChange}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                      placeholder="e.g., Levi's, Nike, Zara"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Size</label>
                    <input
                      type="text"
                      name="size"
                      value={formData.size}
                      onChange={handleInputChange}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                      placeholder="e.g., M, L, 32, 8"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Condition *</label>
                    <select
                      name="condition"
                      required
                      value={formData.condition}
                      onChange={handleInputChange}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                    >
                      <option value="">Select Condition</option>
                      <option value="new">New with Tags</option>
                      <option value="like-new">Like New</option>
                      <option value="excellent">Excellent</option>
                      <option value="good">Good</option>
                      <option value="fair">Fair</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Your Price *</label>
                    <div className="relative">
                      <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500">$</span>
                      <input
                        type="number"
                        name="price"
                        required
                        value={formData.price}
                        onChange={handleInputChange}
                        className="w-full pl-8 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                        placeholder="0.00"
                      />
                    </div>
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Original Price</label>
                    <div className="relative">
                      <span className="absolute left-3 top-1/2 transform -translate-y-1/2 text-gray-500">$</span>
                      <input
                        type="number"
                        name="originalPrice"
                        value={formData.originalPrice}
                        onChange={handleInputChange}
                        className="w-full pl-8 pr-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                        placeholder="0.00"
                      />
                    </div>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Color</label>
                    <input
                      type="text"
                      name="color"
                      value={formData.color}
                      onChange={handleInputChange}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                      placeholder="e.g., Blue, Black, Multi"
                    />
                  </div>

                  <div>
                    <label className="block text-sm font-medium text-gray-700 mb-2">Material</label>
                    <input
                      type="text"
                      name="material"
                      value={formData.material}
                      onChange={handleInputChange}
                      className="w-full px-4 py-3 border border-gray-300 rounded-lg focus:ring-2 focus:ring-green-500 focus:border-green-500"
                      placeholder="e.g., Cotton, Denim, Polyester"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>

          {/* Additional Product Images */}
          <div className="mt-8">
            <h3 className="text-lg font-semibold text-gray-900 mb-4">Additional Product Images</h3>
            <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
              {additionalImages.map((image, index) => (
                <div key={index} className="relative">
                  <div className="relative w-full h-32 rounded-lg overflow-hidden border-2 border-gray-200">
                    <Image
                      src={image || "/placeholder.svg"}
                      alt={`Additional ${index + 1}`}
                      fill
                      className="object-cover"
                    />
                    <button
                      type="button"
                      onClick={() => removeAdditionalImage(index)}
                      className="absolute top-1 right-1 bg-red-500 text-white rounded-full p-1 hover:bg-red-600 transition-colors"
                    >
                      <X className="w-3 h-3" />
                    </button>
                  </div>
                </div>
              ))}

              {additionalImages.length < 4 && (
                <label className="flex flex-col items-center justify-center w-full h-32 border-2 border-gray-300 border-dashed rounded-lg cursor-pointer bg-gray-50 hover:bg-gray-100 transition-colors">
                  <div className="flex flex-col items-center justify-center">
                    <Plus className="w-6 h-6 mb-2 text-gray-400" />
                    <p className="text-xs text-gray-500">Add Image</p>
                  </div>
                  <input
                    type="file"
                    className="hidden"
                    accept="image/*"
                    multiple
                    onChange={handleAdditionalImageUpload}
                  />
                </label>
              )}
            </div>
            <p className="text-sm text-gray-500 mt-2">Add up to 4 additional images to showcase your item</p>
          </div>

          {/* Submit Buttons */}
          <div className="mt-8 flex flex-col sm:flex-row gap-4 justify-end">
            <button
              type="button"
              className="px-6 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 transition-colors"
            >
              Save as Draft
            </button>
            <button
              type="submit"
              className="px-6 py-3 bg-green-600 hover:bg-green-700 text-white rounded-lg transition-colors font-semibold"
            >
              List Item
            </button>
          </div>
        </form>
      </div>
    </div>
  )
}

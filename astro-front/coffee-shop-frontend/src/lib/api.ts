// src/lib/api.ts
import axios from 'axios';
import { authService } from './auth';
import type { Product, PurchaseRecord, SaleRecord, PageResponse } from './types';

const API_URL = 'http://localhost:8080/api';

// Configure axios with auth header
axios.interceptors.request.use(config => {
    const token = authService.getToken();
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Handle errors globally
axios.interceptors.response.use(
    response => response,
    error => {
        const message = error.response?.data?.message || error.message || 'An error occurred';
        return Promise.reject(new Error(message));
    }
);

// Products API
export const getProducts = async (): Promise<Product[]> => {
    const response = await axios.get(`${API_URL}/products`);
    return response.data;
};

export const createProduct = async (productData: Partial<Product>): Promise<Product> => {
    const response = await axios.post(`${API_URL}/products`, productData);
    return response.data;
};

// Transactions API
export const createPurchase = async (purchaseData: {
    productId: number;
    quantity: number;
    price: number;
    transactionDate: string;
}): Promise<PurchaseRecord> => {
    const response = await axios.post(`${API_URL}/transactions/purchase`, purchaseData);
    return response.data;
};

export const createSale = async (saleData: {
    productId: number;
    quantity: number;
    price: number;
    transactionDate: string;
}): Promise<SaleRecord> => {
    const response = await axios.post(`${API_URL}/transactions/sell`, saleData);
    return response.data;
};

export const getPurchases = async (
    page = 0,
    size = 10,
    month?: string
): Promise<PageResponse<PurchaseRecord>> => {
    const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
        ...(month ? { month } : {})
    });
    
    const response = await axios.get(`${API_URL}/transactions/purchases?${params}`);
    return response.data;
};

export const getSales = async (
    page = 0,
    size = 10,
    month?: string
): Promise<PageResponse<SaleRecord>> => {
    const params = new URLSearchParams({
        page: page.toString(),
        size: size.toString(),
        ...(month ? { month } : {})
    });
    
    const response = await axios.get(`${API_URL}/transactions/sellings?${params}`);
    return response.data;
};
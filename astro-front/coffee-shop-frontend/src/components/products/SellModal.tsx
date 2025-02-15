import React, { useState, Fragment } from 'react';
import { Dialog, Transition } from '@headlessui/react';
import { XMarkIcon as X } from '@heroicons/react/24/outline';
import type { Product } from '../../lib/types';
import { createSale } from '../../lib/api';

interface SellModalProps {
  isOpen: boolean;
  onClose: () => void;
  product: Product | null;
  onSellComplete: () => void;
}

const SellModal: React.FC<SellModalProps> = ({
  isOpen,
  onClose,
  product,
  onSellComplete
}) => {
  const [quantity, setQuantity] = useState('1');
  const [price, setPrice] = useState(product?.lastSoldPrice?.toString() || '0');
  const [error, setError] = useState('');

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!product) return;

    setError('');
    try {
      await createSale({
        productId: product.id,
        quantity: parseInt(quantity),
        price: parseFloat(price),
        transactionDate: new Date().toISOString().split('T')[0]
      });

      onSellComplete();
      handleClose();
    } catch (error) {
      setError('Sale failed. Please try again.');
      console.error('Error in sale:', error);
    }
  };

  const handleClose = () => {
    setQuantity('1');
    setPrice(product?.lastSoldPrice?.toString() || '0');
    setError('');
    onClose();
  };

  if (!product) return null;

  return (
    <Transition show={isOpen} as={Fragment}>
      <Dialog as="div" className="relative z-10" onClose={handleClose}>
        <Transition.Child
          as={Fragment}
          enter="ease-out duration-300"
          enterFrom="opacity-0"
          enterTo="opacity-100"
          leave="ease-in duration-200"
          leaveFrom="opacity-100"
          leaveTo="opacity-0"
        >
          <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" />
        </Transition.Child>

        <div className="fixed inset-0 z-10 overflow-y-auto">
          <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
            <Transition.Child
              as={Fragment}
              enter="ease-out duration-300"
              enterFrom="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
              enterTo="opacity-100 translate-y-0 sm:scale-100"
              leave="ease-in duration-200"
              leaveFrom="opacity-100 translate-y-0 sm:scale-100"
              leaveTo="opacity-0 translate-y-4 sm:translate-y-0 sm:scale-95"
            >
              <Dialog.Panel className="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg sm:p-6">
                <div className="absolute right-0 top-0 pr-4 pt-4">
                  <button
                    type="button"
                    className="rounded-md bg-white text-gray-400 hover:text-gray-500"
                    onClick={handleClose}
                  >
                    <span className="sr-only">Close</span>
                    <X className="h-6 w-6" />
                  </button>
                </div>

                <div className="sm:flex sm:items-start">
                  <div className="mt-3 text-center sm:mt-0 sm:text-left w-full">
                    <Dialog.Title as="h3" className="text-lg font-semibold leading-6 text-gray-900">
                      Shit {product.name}
                    </Dialog.Title>
                    <div className="mt-4">
                      <form onSubmit={handleSubmit} className="space-y-4">
                        <div>
                          <label htmlFor="quantity" className="block text-sm font-medium text-gray-700">
                            Sasia
                          </label>
                          <input
                            type="number"
                            id="quantity"
                            value={quantity}
                            onChange={(e) => setQuantity(e.target.value)}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                            min="1"
                            required
                          />
                        </div>

                        <div>
                          <label htmlFor="price" className="block text-sm font-medium text-gray-700">
                            Cmimi per njesi (lek)
                          </label>
                          <input
                            type="number"
                            id="price"
                            value={price}
                            onChange={(e) => setPrice(e.target.value)}
                            className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500 sm:text-sm"
                            min="0"
                            step="1"
                            required
                          />
                        </div>

                        <div className="mt-2 text-sm text-gray-600">
                          Totali i shitjes : {(parseFloat(price) * parseInt(quantity || '0')).toFixed(2)} lek
                        </div>

                        {error && (
                          <div className="text-red-500 text-sm mt-2">
                            {error}
                          </div>
                        )}

                        <div className="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
                          <button
                            type="submit"
                            className="inline-flex w-full justify-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 sm:ml-3 sm:w-auto"
                          >
                            Konfirmo shitjen
                          </button>
                          <button
                            type="button"
                            className="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto"
                            onClick={handleClose}
                          >
                            Anullo
                          </button>
                        </div>
                      </form>
                    </div>
                  </div>
                </div>
              </Dialog.Panel>
            </Transition.Child>
          </div>
        </div>
      </Dialog>
    </Transition>
  );
};

export default SellModal;
package com.agile.share.check.Stock;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class ShareCheck {

	private String[] stockSymbols;
	private int[] stockShares;
	private StockBean[] stockBeans;
	private static boolean threadRunning = false;
	public String[] items;
	public  boolean internetAvailable = false;

	public ShareCheck() {
		stockSymbols = new String[0];
		stockShares = new int[0];
		stockBeans = new StockBean[0];
		items = new String[0];
	}

	public Thread thread = new Thread() {
		@Override
		public void run() {
			try {
				for (int i = 0; i < getStockSymbols().length; i++) {
					getStockBeans()[i] = StockTickerDAO.getInstance()
							.getStockPrice(getStockSymbols()[i]);
				}
			} catch (Exception e) {
				
			} finally {
				threadRunning = true;
			}
		}
	};

	public void checkInternet() {
		internetAvailable = isInternetOn();

		if (internetAvailable) {
			DateString newDate = new DateString();
			System.out.println(newDate.updateDateString());

			if (!threadRunning) {
				threadRunning = true;
				thread.start();
			}
		} 
	}

	public boolean isInternetOn() {
		return true;
	}

	public String UpdateTime() {
		DateString newDate = new DateString();

		System.out.println(newDate.updateDateString());
		return newDate.updateDateString();
	}

	public String convertFloatToBigDecimal(float inputValue) {
		BigDecimal totalShareValue = new BigDecimal(inputValue);
		NumberFormat n = NumberFormat.getCurrencyInstance(Locale.UK);
		String roundedString = n.format(totalShareValue.setScale(0,
				BigDecimal.ROUND_HALF_EVEN));
		roundedString = roundedString.substring(0, roundedString.length() - 3);
		return roundedString;
	}

	public void printStockDetails() {
		System.out.println("<------------------------->");
		System.out.println("Share Value");
		System.out.println("<------------------------->");
		for (int i = 0; i < getStockSymbols().length; i++) {
			System.out.println("Stock name = " + getStockSymbols()[i]
					+ " \tPrice = �" + requestStockPrice(stockBeans[i]));
		}
	}

	public void printTotalShare() {
		System.out.println("<------------------------->");
		System.out.println("Total Share Value");
		System.out.println("<------------------------->");
		for (int i = 0; i < getStockSymbols().length; i++) {
			System.out.println("Stock name = " + getStockSymbols()[i]
					+ " \tPrice = �" + requestStockPrice(stockBeans[i])
					* stockShares[i]);
		}
	}

	public void printStockPortfolioValue() {
		System.out.println("<------------------------->");
		System.out.println("Total Portfolio Value");
		System.out.println("<------------------------->");
		System.out.println("Total Portfolio Value : \t�"
				+ getStockPortfolioTotalValue());
		System.out.println("Total portfolio Value Rounded : �"
				+ Math.round(getStockPortfolioTotalValue()));
		System.out.println("Total portfolio Big Decimal   : "
				+ convertFloatToBigDecimal(Math
						.round(getStockPortfolioTotalValue())));
	}

	public float getTotalStockValueForShare(int position) {
		return requestStockPrice(stockBeans[position]) * stockShares[position];
	}

	// Getters and Setters
	public StockBean[] getStockBeans() {
		return this.stockBeans;
	}

	public void setStockBeans(StockBean[] stockBeanArray) {
		this.stockBeans = stockBeanArray;
	}

	public float requestStockPrice(StockBean stockBean) {
		return (stockBean.getPrice() / 100);
	}

	public float requestStockPriceFromPosition(int position) {
		return (getStockBeans()[position].getPrice() / 100);
	}

	public void requestStockDetails() {
		for (int i = 0; i < getStockSymbols().length; i++) {
			stockBeans[i] = StockTickerDAO.getInstance().getStockPrice(
					getStockSymbols()[i]);
		}
	}

	public float getStockPortfolioTotalValue() {
		float total = 0.0f;

		for (int i = 0; i < getStockSymbols().length; i++) {
			total += (requestStockPrice(stockBeans[i]) * stockShares[i]);
		}
		return total;
	}

	public int getStockShare(int position) {
		return stockShares[position];
	}

	public String getStockSymbol(int position) {
		return stockSymbols[position];
	}

	public String[] getStockSymbols() {
		return stockSymbols;
	}

	public int[] getStockShares() {
		return stockShares;
	}

	public String[] getItems() {
		return items;
	}

	public void setStockSymbols(String[] stockSymbols) {
		this.stockSymbols = stockSymbols;
	}

	public void setStockShares(int[] stockShares) {
		this.stockShares = stockShares;
	}

	public void setItems(String[] items) {
		this.items = items;
	}
}

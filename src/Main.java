import java.util.ArrayList;
import java.util.Scanner;

class Car{
	private String CarId;
	private String model;
	private String brand;
	private int DayPrice;
	private boolean isAvailable;
	public Car(String CarId, String model, String brand, int DayPrice, boolean isAvailable){
		this.CarId = CarId;
		this.model = model;
		this.brand = brand;
		this.DayPrice = DayPrice;
		this.isAvailable = true;
	}
	public String getCarId(){
		return CarId;
	}
	public String getModel(){
		return model;
	}
	public String getBrand(){
		return brand;
	}
	public double calculatePrice(int days){
		return DayPrice * days;
	}
	public boolean isAvailable(){
		return isAvailable;
	}
	public void rented(){
		isAvailable = false;
	}
	public void returned(){
		isAvailable = true;
	}
}
class Customer{
	private String customerId;
	private String name;
	public Customer(String customerId, String name){
		this.customerId = customerId;
		this.name = name;
	}
	public String getCustomerId(){
		return customerId;
	}
	public String getName(){
		return name;
	}

}
class Rental{
	private Car car;
	private Customer customer;
	private int days;
	public Rental(Car car, Customer customer, int days){
		this.car = car;
		this.customer = customer;
		this.days = days;
	}
	public Car getCar(){
		return car;
	}
	public Customer getCustomer(){
		return customer;
	}
	public int getDays(){
		return days;
	}
}
class RentalService{
	private ArrayList<Car> cars;
	private ArrayList<Customer> customers;
	private ArrayList<Rental> rentals;
	public RentalService(){
		cars = new ArrayList<>();
		customers = new ArrayList<>();
		rentals = new ArrayList<>();
	}
	public void addCar(Car car){
		cars.add(car);
	}
	public void addCustomer(Customer customer){
		customers.add(customer);
	}
	public void rentCar(Car car, Customer customer, int days){
		if(car.isAvailable()){
			car.rented();
			rentals.add(new Rental(car, customer, days));
		} else {
			System.out.println("Currently there is no car available for rent.");
		}
	}
	public void returnCar(Car car){
		car.returned();
		Rental RemoveRental = null;
		for (Rental rental : rentals) {
			if(rental.getCar().getCarId() == car.getCarId()){
				RemoveRental = rental;
				break;
			}
		}
		if(RemoveRental != null){
			rentals.remove(RemoveRental);
		} else {
			System.out.println("This car was not rented.");
		}
	}

	public void menu(){
		Scanner scanner = new Scanner(System.in);
		while(true){
			System.out.println("---- Car Rental Service ----");
			System.out.println("1. Rent a car");
			System.out.println("2. Return a car");
			System.out.println("3. Exit");
			System.out.print("Enter your choice: ");

			int choice = scanner.nextInt();
			scanner.nextLine();
			if(choice == 1){
				System.out.println("\n---- Rent a car ----");
				System.out.print("Enter your name: ");
				String name = scanner.nextLine();

				System.out.print("\nAvailable cars: \n");
				for (Car car : cars) {
					if(car.isAvailable()){
						System.out.println(car.getCarId() + " - " + car.getBrand() + " " + car.getModel());
					}
				}
				System.out.print("\nEnter car Id you want to rent: ");
				String carId = scanner.nextLine();

				System.out.print("Enter number of days you want to rent: ");
				int days = scanner.nextInt();
				scanner.nextLine();

				Customer newcustomer = new Customer("CNR" + (customers.size() + 1), name);
				addCustomer(newcustomer);

				Car selectedCar = null;
				for (Car car : cars) {
					if(car.getCarId().equals(carId) && car.isAvailable()){
						selectedCar = car;
						break;
					}
				}
				if (selectedCar != null) {
					double totalprice = selectedCar.calculatePrice(days);
					System.out.println("\n-- Rental Information --\n");
					System.out.println("Customer ID: " + newcustomer.getCustomerId());
					System.out.println("Customer Name: " + newcustomer.getName());
					System.out.println("Car: " + selectedCar.getBrand() + " " + selectedCar.getModel());
					System.out.println("Rental Days: " + days);
					System.out.printf("Total Price: $%.2f%n", totalprice);

					System.out.print("\nConfirm rental (Y/N): ");
					String confirm = scanner.nextLine();
					if(confirm.equalsIgnoreCase("Y")){
						rentCar(selectedCar, newcustomer, days);
						System.out.println("\nCar rented successfully.");
					} else {
						System.out.println("\nRental canceled.");
					}
				} else {
					System.out.println("\nCar not found or not available for rent.");
				}
			} else if(choice == 2){
				System.out.println("\n---- Return a car ----");
				System.out.print("Enter car Id you want to return: ");
				String carId = scanner.nextLine();

				Car cartoretun = null;
				for (Car car : cars) {
					if(car.getCarId().equals(carId) && !car.isAvailable()){
						cartoretun = car;
						break;
					}
				}
				if (cartoretun != null) {
					Customer customer = null;
					for (Rental rental : rentals) {
						if(rental.getCar().getCarId().equals(carId)){
							customer = rental.getCustomer();
							break;
						}
					}
					if (customer != null) {
						returnCar(cartoretun);
						System.out.println("Car returned successfully by " + customer.getName());
					} else {
						System.out.println("This car was not rented or rental information is missing.");
					}
				} else {
					System.out.println("Invalid car ID or car is not rented.");
				}
			} else if(choice == 3){
				System.out.println("\nExiting...");
				break;
			} else {
				System.out.println("\nInvalid choice. Please try again.");

			}

		}
	}
}


public class Main {
	public static void main(String[] args) {
		RentalService rentalService = new RentalService();
		rentalService.addCar(new Car("CR001", "Civic", "Honda", 100, true));
		rentalService.addCar(new Car("CR002", "Corolla", "Toyota", 120, true));
		rentalService.addCar(new Car("CR003", "Vitz", "Toyota", 80, true));
		Car car1 = new Car("CR004", "Aqua", "Toyota", 90, true);
		rentalService.addCar(car1);

		rentalService.menu();

	}
}
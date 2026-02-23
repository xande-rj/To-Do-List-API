package alexandreS.To_Do_List_API;

import org.springframework.boot.SpringApplication;

public class TestToDoListApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(ToDoListApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}

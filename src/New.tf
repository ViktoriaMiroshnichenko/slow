# Define the provider (AWS in this case)
provider "aws" {
  region = "us-west-2"  # Specify the AWS region
}

# Define a resource: an EC2 instance
resource "aws_instance" "example" {
  ami           = "ami-0c55b159cbfafe1f0"  # Specify the AMI ID (Amazon Machine Image)
  instance_type = "t2.micro"  # Specify the instance type

  # Add tags to the instance
  tags = {
    Name = "ExampleInstance"
  }
}


# Output the public IP of the instance
output "instance_ip" {
  description = "The public IP address of the EC2 instance"
  value       = aws_instance.example.public_ip
}

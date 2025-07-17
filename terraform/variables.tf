variable "aws_region" {

  description = "The AWS region to deploy resources in"
  type        = string
  default     = "us-east-1"

}

// VPC configuration
variable "cidr_block" {
  description = "The CIDR block for the VPC"
  type        = string


}

variable "vpc_name" {
  description = "The name of the VPC"
  type        = string


}

variable "public_subnet_name" {
  description = "The name of the public subnet"
  type        = string
  default     = "public-subnet"

}

variable "private_subnet_a" {
  description = "The name of the private subnet"
  type        = string
  default     = "private-subnet_1b"

}

variable "private_subnet_b" {
  description = "The name of the private subnet"
  type        = string
  default     = "private-subnet_1b"

}

variable "public_route_table_name" {
  description = "The name of the route table"
  type        = string
  default     = "public-subnet-route-table"

}

variable "private_route_table_name" {
  description = "The name of the private route table"
  type        = string
  default     = "private-subnet-route-table"

}

variable "aws_internet_gateway_name" {
  description = "The name of the internet gateway"
  type        = string
  default     = "bastion-internet-gateway"

}

variable "eip_id" {
  description = "The ID of the Elastic IP"
  type        = string

}

variable "igw_destination_cidr_block" {
  description = "The destination CIDR block for the internet gateway"
  type        = string
  default     = "0.0.0.0/0"
}

variable "nat_gateway_name" {
  description = "The name of the NAT gateway"
  type        = string

}

variable "public_sg_name" {
  description = "The name of the public security group"
  type        = string


}

variable "private_sg_name" {
  description = "The name of the private security group"
  type        = string

}

/*------------------- Compute Layer ------------------------------------------- */
variable "instance_type" {
  description = "The type of the instance"
  type        = string
  default     = "t2.micro"

}

variable "ssh_key_name" {
  sensitive   = true
  description = "Name of the SSH key pair"
  type        = string

}

variable "public_server_name" {
  description = "The name of the public server"
  type        = string

}

variable "private_server_name" {
  description = "The name of the private server"
  type        = string

}

variable "db_admin_user" {
  description = "Data Base Admin User"
  type        = string
}

variable "db_password" {
  description = "database password"
  type        = string
}

// Networking Architecture Configuration
resource "aws_vpc" "this" {
  cidr_block = var.cidr_block

  tags = {
    Name = var.vpc_name
  }
}


resource "aws_subnet" "public_subnet" {

  vpc_id = aws_vpc.this.id
  # cidr block using terraform functions using the primary vpc cidr block cidrsubnet is a function
  cidr_block = cidrsubnet(aws_vpc.this.cidr_block, 8, 0)

  tags = {
    Name = var.public_subnet_name
  }
}

// private subnet for the mutiple AZ
resource "aws_subnet" "private_subnet_a" {
  vpc_id     = aws_vpc.this.id
  cidr_block = cidrsubnet(aws_vpc.this.cidr_block, 8, 1)

  tags = {
    Name = var.private_subnet_a
  }
}


resource "aws_subnet" "private_subnet_b" {
  vpc_id     = aws_vpc.this.id
  cidr_block = cidrsubnet(aws_vpc.this.cidr_block, 8, 2)

  tags = {
    Name = var.private_subnet_b
  }
}

resource "aws_route_table" "public_route_table" {
  vpc_id = aws_vpc.this.id

  tags = {
    name = var.public_route_table_name
  }

}

resource "aws_route_table" "private_route_table" {
  vpc_id = aws_vpc.this.id
  tags = {
    name = var.private_route_table_name
  }

}

// Inernet gateway configuration allow e-gress inernet access to private subnetre
resource "aws_internet_gateway" "this" {
  vpc_id = aws_vpc.this.id

  tags = {
    Name = var.aws_internet_gateway_name
  }

}

/* -------------------------- Routing Layer ---------------------------------------------------*/
resource "aws_route_table_association" "public_rt_association" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_route_table.id

}

// routing connection for the public subnet to the internet gateway
resource "aws_route" "public_rt" {
  route_table_id         = aws_route_table.public_route_table.id
  destination_cidr_block = var.igw_destination_cidr_block
  gateway_id             = aws_internet_gateway.this.id

}


/*resource "aws_route_table_association" "private_rt_association_a" {
  subnet_id      = aws_subnet.private_subnet_a.id
  route_table_id = aws_route_table.private_route_table.id
  // the income traffic to the private subnet
} */



resource "aws_security_group" "public_sg" {
  name   = var.public_sg_name
  vpc_id = aws_vpc.this.id

  tags = {
    Name = "allow_tls"
  }


  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # HTTP — limit this in prod
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Secure: allow SSH only from your IP


  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"          // all traffic
    cidr_blocks = ["0.0.0.0/0"] // all traffic to the internet
  }

}

resource "aws_security_group" "private_sg" {
  name   = var.private_sg_name
  vpc_id = aws_vpc.this.id

  tags = {
    Name = "allow_all"
  }

  ingress {
    from_port = 3306
    to_port   = 3306
    protocol  = "tcp"
    #allow only income traffic form the application
    security_groups = [aws_security_group.public_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

}

// Grouping private instance for multiple Az

resource "aws_db_subnet_group" "this" {
  subnet_ids = [
    aws_subnet.private_subnet_a.id, # AZ-1
    aws_subnet.private_subnet_b.id  # AZ-2
  ]
}

/* -------------------------- Compute Layer ---------------------------------------------------*/

//Compute Architecture Configuration

resource "aws_instance" "public_instance" {

  ami                         = data.aws_ami.ubuntu.id
  associate_public_ip_address = true
  instance_type               = var.instance_type
  key_name                    = data.aws_key_pair.this.key_name
  subnet_id                   = aws_subnet.public_subnet.id
  security_groups             = [aws_security_group.public_sg.id]


  tags = {
    Name = var.public_server_name
  }

}

resource "aws_db_instance" "app_db" {
  identifier              = "myapp-db"
  engine                  = "mysql"
  instance_class          = "db.t3.micro"
  allocated_storage       = 20
  username                = var.db_admin_user
  password                = var.db_password
  vpc_security_group_ids  = [aws_security_group.private_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.this.name
  backup_retention_period = 7
  multi_az                = false #optional
}
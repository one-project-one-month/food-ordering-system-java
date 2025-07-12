
// Networking Architecture Configuration
resource "aws_vpc" "this" {
  cidr_block = var.cidr_block


  tags = {
    Name = var.vpc_name
  }
}


//subnet coqnfiguration App
resource "aws_subnet" "public_subnet" {

  vpc_id = aws_vpc.this.id
  # cidr block using terraform functions using the primary vpc cidr block cidrsubnet is a function
  cidr_block = cidrsubnet(aws_vpc.this.cidr_block, 8, 0)

  tags = {
    Name = var.public_subnet_name
  }
}

// DB
resource "aws_subnet" "private_subnet" {
  vpc_id     = aws_vpc.this.id
  cidr_block = cidrsubnet(aws_vpc.this.cidr_block, 8, 1)

  tags = {
    Name = var.private_subnet_name
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

// NAT gateway are created in the public subnet to allow private subnet to access the internet
resource "aws_nat_gateway" "this" {

  allocation_id = data.aws_eip.this.id
  #NAT gateway is created in the public subnet
  subnet_id = aws_subnet.public_subnet.id
  tags = {
    Name = var.nat_gateway_name

  }
  depends_on = [aws_internet_gateway.this]
}


/* -------------------------- Routing Layer ---------------------------------------------------*/
resource "aws_route_table_association" "public_rt_association" {
  subnet_id      = aws_subnet.public_subnet.id
  route_table_id = aws_route_table.public_route_table.id

}

// routing connection for the public subnet to the internet gateway
resource "aws_route" "public_rt" {
  route_table_id         = aws_route_table.public_route_table.id
  destination_cidr_block = var.igw_destination_cidr_block // the income traffic to the private subnet
  gateway_id             = aws_internet_gateway.this.id

}



resource "aws_route_table_association" "private_rt_association" {
  subnet_id      = aws_subnet.private_subnet.id
  route_table_id = aws_route_table.private_route_table.id
  // the income traffic to the private subnet
}

resource "aws_route" "private_rt" {
  route_table_id         = aws_route_table.private_route_table.id
  destination_cidr_block = var.igw_destination_cidr_block // this is also internet traffic but outbound only using nat gateway
  nat_gateway_id         = aws_nat_gateway.this.id        // for network restriction
}


resource "aws_security_group" "public_sg" {
  name   = var.public_sg_name
  vpc_id = aws_vpc.this.id

  tags = {
    Name = "allow_tls"
  }

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"         // SSH port
    cidr_blocks = ["0.0.0.0/0"] // all coming from internet
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
    from_port = 22
    to_port   = 22
    protocol  = "tcp"
    #white listing the income traffic from public server. Using secuiryt group id cause the server IPs can various don't use for the white listing
    security_groups = [aws_security_group.public_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

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

resource "aws_instance" "private_instance" {

  ami             = data.aws_ami.ubuntu.id
  instance_type   = var.instance_type
  key_name        = data.aws_key_pair.this.key_name
  subnet_id       = aws_subnet.private_subnet.id
  security_groups = [aws_security_group.private_sg.id]

  tags = {
    Name = var.private_server_name
  }

}

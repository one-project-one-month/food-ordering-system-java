//lookup data source to get the Elastic IP
data "aws_eip" "this" {
  public_ip = true
  id        = var.eip_id
}

// look up ami image form the aws console

data "aws_ami" "ubuntu" {
  most_recent = true

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }

  owners = ["099720109477"] # Canonical
}


// SSH key paired never generate with Terraform cause of high sensitivity just look up the existing one
data "aws_key_pair" "this" {
  key_name = var.ssh_key_name
  # This will look up the existing SSH key pair in your AWS account
  # Make sure the key pair exists before running this Terraform configuration
}
